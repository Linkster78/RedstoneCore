package com.tek.rcore.item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.tek.rcore.misc.ReflectionUtils;

public class SkullFactory {
	
	private static String USERNAME_ENDPOINT = "https://api.mojang.com/users/profiles/minecraft/%s";
	private static String PROFILE_ENDPOINT = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
	private static String USER_AGENT = "RedstoneCore/1.0";
	private static String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789";
	private static List<CachedSkull> cachedSkulls;
	
	public static ItemStack createSkull(String name) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skull = (SkullMeta) item.getItemMeta();
		Optional<CachedSkull> cachedSkullOpt = getCachedSkull(name);
		if(cachedSkullOpt.isPresent()) {
			try {
				ReflectionUtils.applyProfile(skull, getGameProfile(cachedSkullOpt.get()));
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
			item.setItemMeta(skull);
		} else {
			OfflinePlayer player = Bukkit.getPlayer(name);
			if(player != null) {
				skull.setOwningPlayer(player);
				item.setItemMeta(skull);
			} else {
				try {
					UUID uuid = getUUIDFromName(name);
					if(uuid != null) {
						GameProfile profile = getGameProfile(uuid);
						if(profile != null) {
							ReflectionUtils.applyProfile(skull, profile);
						}
					}
				} catch(ReflectiveOperationException | IOException e) {
					e.printStackTrace();
				}
				item.setItemMeta(skull);
			}
		}
		return item;
	}
	
	public static ItemStack createSkull(UUID uuid) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skull = (SkullMeta) item.getItemMeta();
		Optional<CachedSkull> cachedSkullOpt = getCachedSkull(uuid);
		if(cachedSkullOpt.isPresent()) {
			try {
				ReflectionUtils.applyProfile(skull, getGameProfile(cachedSkullOpt.get()));
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
			item.setItemMeta(skull);
		} else {
			OfflinePlayer player = Bukkit.getPlayer(uuid);
			if(player != null) {
				skull.setOwningPlayer(player);
				item.setItemMeta(skull);
			} else {
				try {
					GameProfile profile = getGameProfile(uuid);
					if(profile != null) {
						ReflectionUtils.applyProfile(skull, profile);
					}
				} catch(ReflectiveOperationException | IOException e) {
					e.printStackTrace();
				}
				item.setItemMeta(skull);
			}
		}
		return item;
	}
	
	public static ItemStack createSkull(GameProfile profile) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skull = (SkullMeta) item.getItemMeta();
		try {
			ReflectionUtils.applyProfile(skull, profile);
		} catch(ReflectiveOperationException e) {
			e.printStackTrace();
		}
		item.setItemMeta(skull);
		return item;
	}
	
	public static GameProfile getFakeGameProfileFromProperties(String textureValue, String textureSignature) {
		GameProfile profile = new GameProfile(UUID.randomUUID(), randomName());
		profile.getProperties().put("textures", new Property("textures", textureValue, textureSignature));
		return profile;
	}
	
	public static GameProfile getFakeGameProfileFromTextureValue(String textureValue) {
		GameProfile profile = new GameProfile(UUID.randomUUID(), randomName());
		profile.getProperties().put("textures", new Property("textures", textureValue));
		return profile;
	}
	
	public static GameProfile getFakeGameProfileFromMinecraftSkinUrl(String url) {
		GameProfile profile = new GameProfile(UUID.randomUUID(), randomName());
		String formattedUrl = String.format("{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}", url);
		String encodedUrl = Base64.getEncoder().encodeToString(formattedUrl.getBytes());
		profile.getProperties().put("textures", new Property("textures", encodedUrl));
		return profile;
	}
	
	public static GameProfile getGameProfile(CachedSkull cachedSkull) {
		GameProfile profile = new GameProfile(cachedSkull.getUuid(), cachedSkull.getName());
		profile.getProperties().put("textures", new Property("textures", cachedSkull.getTextureValue(), cachedSkull.getTextureSignature()));
		return profile;
	}
	
	public static GameProfile getGameProfile(UUID uuid) throws IOException {
		JsonObject sessionProfile = getSessionProfile(uuid);
		
		if(sessionProfile != null) {
			GameProfile profile = new GameProfile(uuid, sessionProfile.get("name").getAsString());
			
			String textureValue = null;
			String textureSignature = null;
			JsonArray properties = sessionProfile.get("properties").getAsJsonArray();
			Iterator<JsonElement> elementIterator = properties.iterator();
			while(elementIterator.hasNext()) {
				JsonElement arrayElement = elementIterator.next();
				JsonObject property = (JsonObject) arrayElement;
				if(property.get("name").getAsString().equals("textures")) {
					textureValue = property.get("value").getAsString();
					textureSignature = property.get("signature").getAsString();
					break;
				}
			}
			
			if(textureValue != null && textureSignature != null) {
				profile.getProperties().put("textures", new Property("textures", textureValue, textureSignature));
				Optional<CachedSkull> cachedSkullOpt = getCachedSkull(uuid);
				if(!cachedSkullOpt.isPresent()) cachedSkulls.add(new CachedSkull(uuid, 
						sessionProfile.get("name").getAsString(), textureValue, textureSignature));
			}
			
			return profile;
		} else {
			return null;
		}
	}
	
	public static UUID getUUIDFromName(String name) throws IOException {
		URL url = new URL(String.format(USERNAME_ENDPOINT, name));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", USER_AGENT);
		
		if(connection.getResponseCode() == 429) {
			return null;
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
		String response = "";
		String line;
		while((line = reader.readLine()) != null) {
			response += line;
		}
		
		reader.close();
		
		JsonElement parsed = new JsonParser().parse(response);
		if(parsed instanceof JsonObject) {
			JsonObject jsonResponse = (JsonObject) parsed;
			String uuidString = jsonResponse.get("id").getAsString();
			if(uuidString.length() == 32) {
				String formattedUUID = uuidString.substring(0, 8) + "-" + uuidString.substring(8, 12) + "-" + 
						uuidString.substring(12, 16) + "-" + uuidString.substring(16, 20) + "-" + uuidString.substring(20, 32);
				return UUID.fromString(formattedUUID);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static JsonObject getSessionProfile(UUID uuid) throws IOException {
		URL url = new URL(String.format(PROFILE_ENDPOINT, uuid.toString().replace("-", "")));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", USER_AGENT);
		
		if(connection.getResponseCode() == 429) {
			return null;
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
		String response = "";
		String line;
		while((line = reader.readLine()) != null) {
			response += line;
		}
		
		reader.close();
		
		JsonElement parsed = new JsonParser().parse(response);
		if(parsed instanceof JsonObject) {
			return (JsonObject) parsed;
		} else {
			return null;
		}
	}
	
	public static String randomName() {
		String randomized = "";
		SecureRandom random = new SecureRandom();
		for(int i = 0; i < 16; i++) {
			randomized += ALPHABET.charAt(random.nextInt(ALPHABET.length()));
		}
		return randomized;
	}
	
	public static Optional<CachedSkull> getCachedSkull(UUID uuid) {
		return cachedSkulls.stream().filter(cs -> cs.getUuid().equals(uuid)).findFirst();
	}
	
	public static Optional<CachedSkull> getCachedSkull(String name) {
		return cachedSkulls.stream().filter(cs -> cs.getName().equalsIgnoreCase(name)).findFirst();
	}
	
	static {
		cachedSkulls = new ArrayList<CachedSkull>();
	}
	
	public static class CachedSkull {
		
		private UUID uuid;
		private String name;
		private String textureValue;
		private String textureSignature;
		
		public CachedSkull(UUID uuid, String name, String textureValue, String textureSignature) {
			this.uuid = uuid;
			this.name = name;
			this.textureValue = textureValue;
			this.textureSignature = textureSignature;
		}

		public UUID getUuid() {
			return uuid;
		}

		public String getName() {
			return name;
		}

		public String getTextureValue() {
			return textureValue;
		}

		public String getTextureSignature() {
			return textureSignature;
		}
		
	}
	
	public static enum NumberSkulls {
		
		/*
		 * Huge thanks to the awesome folks at minecraft-heads.com
		 * for providing a list of heads! This is just a small list
		 * of the numbers with a bunch of different colors.
		 */
		
		BROWN_0(NumberSet.BROWN, 0, "7bafba8a3d36476851e3655bbac87de2e718e85fc8466b2ba701aaad8cdb142"),
		BROWN_1(NumberSet.BROWN, 1, "3889921dfb3d93b10acc79dfbefed463c729e62b1f76a3dcce1b6c44012b"),
		BROWN_2(NumberSet.BROWN, 2, "c6dde76e6f88665b681794b4b914ac513e57c272dd0b2f988f47b7482d4b177"),
		BROWN_3(NumberSet.BROWN, 3, "8b959649e1f9a20658c4142d181162014bf34710be91cb4d495bc74a7e127a"),
		BROWN_4(NumberSet.BROWN, 4, "39240d3711b3f81134851b9228c5a3045788e7aba7014719b687f12857c538"),
		BROWN_5(NumberSet.BROWN, 5, "1926e41525d7219cfbf83b25c187251555dbf351369f95a25aadd70bc9dd7dd"),
		BROWN_6(NumberSet.BROWN, 6, "905aaebbbc7fe93b6d51884bbded39f9a7c5467a86299f7fa0ffbd13e7b94e"),
		BROWN_7(NumberSet.BROWN, 7, "dc4315942cb7ced179f13175951bd5ef91d42b534764b63bf840354086a5a7"),
		BROWN_8(NumberSet.BROWN, 8, "474ae33a4d1cf8a1bd2bc73e1361f5691cbcabb6d93c16b8c287bc757025"),
		BROWN_9(NumberSet.BROWN, 9, "b1d38a88cfa569f2a1545275fce4fd1869f75cb4853df510fc3dbe63be891e"),
	
		CYAN_0(NumberSet.CYAN, 0, "7a132956bcbf95213a521aa93456d4a15c79cf1736ecbabfe81e5f5e44"),
		CYAN_1(NumberSet.CYAN, 1, "7347b7a5a6bb6419d33eb46102650cc91bfdc159d8ff1f55996d4f0ad5bc758"),
		CYAN_2(NumberSet.CYAN, 2, "20ba412aa76a8e119ae3eca071428f3d9e2042b4952bd83358d6296e812be8"),
		CYAN_3(NumberSet.CYAN, 3, "36dbfdfac15e284c2ec02e38911b4ec5d815bfb1e5eee522d557af35d840dc"),
		CYAN_4(NumberSet.CYAN, 4, "a7ac53233ae842aebb38c13becce7c91f8d5f67976332ce1ba88dc0e682f8d4"),
		CYAN_5(NumberSet.CYAN, 5, "2a67144b323ad4dcebfd1c6a630743c702c4da4552289a92421eafc4c2d4b43"),
		CYAN_6(NumberSet.CYAN, 6, "412aab401d69a5f0c5cadcb3c1d137b10794c43b058f93630b8145b83497a40"),
		CYAN_7(NumberSet.CYAN, 7, "44e2824eed253ac932ca81df7efc5106bd465c349a18bde699638c1f146f54"),
		CYAN_8(NumberSet.CYAN, 8, "64588d8efd99511b8d2ca395648d322fa8cdc98d42bd79e3c0d6103c948e68"),
		CYAN_9(NumberSet.CYAN, 9, "7c2a51c4df7829b05bac9b38a99c7273b1a3b1d46bfb2a9c65e61740bf6244ac"),
		
		GRAY_0(NumberSet.GRAY, 0, "a3a487b1f81c9ecc6e18857c6566529e7efa23eef59814fe57d64df8e2cf1"),
		GRAY_1(NumberSet.GRAY, 1, "bf61269735f1e446becff25f9cb3c823679719a15f7f0fbc9a03911a692bdd"),
		GRAY_2(NumberSet.GRAY, 2, "7d81a32d978f933deb7ea26aa326e4174697595a426eaa9f2ae5f9c2e661290"),
		GRAY_3(NumberSet.GRAY, 3, "ceadaded81563f1c87769d6c04689dcdb9e8ca01da35281cd8fe251728d2d"),
		GRAY_4(NumberSet.GRAY, 4, "6c608c2db525d6d77f7de4b961d67e53e9d7bacdaff31d4ca10fbbf92d66"),
		GRAY_5(NumberSet.GRAY, 5, "1144c5193435199c135bd47d166ef1b4e2d3218383df9d34e3bb20d9f8e593"),
		GRAY_6(NumberSet.GRAY, 6, "f61f7e38556856eae5566ef1c44a8cc64af8f3a58162b1dd8016a8778c71c"),
		GRAY_7(NumberSet.GRAY, 7, "6e1cf31c49a24a8f37849fc3c5463ab64cc9bceb6f276a5c44aedd34fdf520"),
		GRAY_8(NumberSet.GRAY, 8, "61c9c09d52debc465c32542c68be42bda6f6753fe1deba257327ac5a0c3ad"),
		GRAY_9(NumberSet.GRAY, 9, "2dcf39f4bcd98484b0b479a7992d9270fe3a59b9b1a806d7a64ffb5b551ad"),
		
		GREEN_0(NumberSet.GREEN, 0, "24581d3955e9acd513d28dd32257ae51ff7fd6df05b5f4b921f1deae49b2172"),
		GREEN_1(NumberSet.GREEN, 1, "6d65ce83f1aa5b6e84f9b233595140d5b6beceb62b6d0c67d1a1d83625ffd"),
		GREEN_2(NumberSet.GREEN, 2, "dd54d1f8fbf91b1e7f55f1bdb25e2e33baf6f46ad8afbe08ffe757d3075e3"),
		GREEN_3(NumberSet.GREEN, 3, "21e4ea59b54cc99416bc9f624548ddac2a38eea6a2dbf6e4ccd83cec7ac969"),
		GREEN_4(NumberSet.GREEN, 4, "8b527b24b5d2bcdc756f995d34eae579d7414b0a5f26c4ffa4a558ecaf6b7"),
		GREEN_5(NumberSet.GREEN, 5, "84c8c3710da2559a291adc39629e9ccea31ca9d3d3586bfea6e6e06124b3c"),
		GREEN_6(NumberSet.GREEN, 6, "e2113c604a22b224fbd3597f904a7f9227a7c1ae53439c96994bfa23b52eb"),
		GREEN_7(NumberSet.GREEN, 7, "24bde79f84fc5f3f1fbc5bc01071066bd20cd263a1654d64d60d84248ba9cd"),
		GREEN_8(NumberSet.GREEN, 8, "f2ee1371d8f0f5a8b759c291863d704adc421ad519f17462b87704dbf1c78a4"),
		GREEN_9(NumberSet.GREEN, 9, "4378f2ed773cd6b2551819218bff87c374a4b7d6f3b2c236787ea79367bf6d1c"),
		
		LIGHT_BLUE_0(NumberSet.LIGHT_BLUE, 0, "6cf4c58b50a537b45ce7d511fdb2642e50ea36c2f9ef30dba8d63e35c542d59"),
		LIGHT_BLUE_1(NumberSet.LIGHT_BLUE, 1, "aebd85b7f9e6222b395451adb46b76a62f7e95da6fbf7efe715c783455140"),
		LIGHT_BLUE_2(NumberSet.LIGHT_BLUE, 2, "502eba83a6a953917f1d991bb379b36e7ff04a496d17f5424d04aa12efbab"),
		LIGHT_BLUE_3(NumberSet.LIGHT_BLUE, 3, "e1ff52d15e7a7758b03c6fa861acbce93ae5662f7578261540b392de4157b29"),
		LIGHT_BLUE_4(NumberSet.LIGHT_BLUE, 4, "884b63d848dcbcf061e642f697b46d599a942e36e785347efec9534cc76d8a66"),
		LIGHT_BLUE_5(NumberSet.LIGHT_BLUE, 5, "9d3c54e9f12cd98449ddda0689bec179bd58e34fcf91d85809a9f96c68fe"),
		LIGHT_BLUE_6(NumberSet.LIGHT_BLUE, 6, "3984b925c3cc38cfacafba753ebd1a39ea63f927310aee380e270fe2c2818"),
		LIGHT_BLUE_7(NumberSet.LIGHT_BLUE, 7, "2f9b635624136e418a636b6bf28e59d17f713763f82daf453676704be0e46581"),
		LIGHT_BLUE_8(NumberSet.LIGHT_BLUE, 8, "cc4856d4fe5a99c7235375d3b359eb191426843555e6da488e9fd5f837be55a"),
		LIGHT_BLUE_9(NumberSet.LIGHT_BLUE, 9, "65871dd8827ea74896c712f3826efd806fd1f49d14926d0635a3be6b9cf3b"),
		
		LIGHT_GRAY_0(NumberSet.LIGHT_GRAY, 0, "ffa45911b16298cfca4b2291eeda666113bc6f2a37dcb2ecd8c2754d24ef6"),
		LIGHT_GRAY_1(NumberSet.LIGHT_GRAY, 1, "caf1b280cab59f4469dab9f1a2af7927ed96a81df1e24d50a8e3984abfe4044"),
		LIGHT_GRAY_2(NumberSet.LIGHT_GRAY, 2, "e4b1e1d426123ce40cd6a54b0f876ad30c08539cf5a6ea63e847dc507950ff"),
		LIGHT_GRAY_3(NumberSet.LIGHT_GRAY, 3, "904ccf8b5332c196c9ea02b22b39b99facd1cc82bfe3f7d7aeedc3c3329039"),
		LIGHT_GRAY_4(NumberSet.LIGHT_GRAY, 4, "6b4fc18e975f4f222d885216e363adc9e6d456aa29080e48eb47144dda436f7"),
		LIGHT_GRAY_5(NumberSet.LIGHT_GRAY, 5, "1d8b22239712e0ad579a62ae4c115103e7728825e17508acd6cc89174ee838"),
		LIGHT_GRAY_6(NumberSet.LIGHT_GRAY, 6, "9eefbad16712a05f98e4f0de5b4486af3987b46ea6ab4e3be93d14a832c56e"),
		LIGHT_GRAY_7(NumberSet.LIGHT_GRAY, 7, "a3e69fa942df3d5ea53a3a97491617510924c6b8d7c4371197378a1cf2def27"),
		LIGHT_GRAY_8(NumberSet.LIGHT_GRAY, 8, "7d184fd4ab51d4622f49b54ce7a1395c29f02ad35ce5abd5d3c25638f3a82"),
		LIGHT_GRAY_9(NumberSet.LIGHT_GRAY, 9, "1b2454a5faa25f7c4f5771d52bb4f55deb1939f75efd8e0ac421812ba3dc7"),
		
		LIME_0(NumberSet.LIME, 0, "defa4a74447d0e2fa4c43f03d9661a7ee6dee51da82e02ea53aad93f715292"),
		LIME_1(NumberSet.LIME, 1, "88991697469653c9af8352fdf18d0cc9c67763cfe66175c1556aed33246c7"),
		LIME_2(NumberSet.LIME, 2, "5496c162d7c9e1bc8cf363f1bfa6f4b2ee5dec6226c228f52eb65d96a4635c"),
		LIME_3(NumberSet.LIME, 3, "c4226f2eb64abc86b38b61d1497764cba03d178afc33b7b8023cf48b49311"),
		LIME_4(NumberSet.LIME, 4, "f920ecce1c8cde5dbca5938c5384f714e55bee4cca866b7283b95d69eed3d2c"),
		LIME_5(NumberSet.LIME, 5, "a2c142af59f29eb35ab29c6a45e33635dcfc2a956dbd4d2e5572b0d38891b354"),
		LIME_6(NumberSet.LIME, 6, "24ddb03aa8c584168c63ece337aefb281774377db72337297de258b4cca6fba4"),
		LIME_7(NumberSet.LIME, 7, "d7de70b88368ce23a1ac6d1c4ad9131480f2ee205fd4a85ab2417af7f6bd90"),
		LIME_8(NumberSet.LIME, 8, "42647ae47b6b51f5a45eb3dcafa9b88f288ede9cebdb52a159e3110e6b8118e"),
		LIME_9(NumberSet.LIME, 9, "dae461a4434196d37296ad5adf6d9d5744a0415dc61c475a6dfa6285814052"),
		
		MAGENTA_0(NumberSet.MAGENTA, 0, "f94385dfe58cf1a73cc60949cc680e36556d1b8ff83f9e18197364d3c78a3"),
		MAGENTA_1(NumberSet.MAGENTA, 1, "872f454c72178181f86ae7d2fbbbc11eaa65e9ae39b9dd77dd803c1f44b6c"),
		MAGENTA_2(NumberSet.MAGENTA, 2, "1577c294e23df158904a582ed1991a5a30c0657e78e2ed43e126f8d5ad21ce8b"),
		MAGENTA_3(NumberSet.MAGENTA, 3, "149eeb7546fccd145254c5682497b8a9a82297c9b60567230f3d17f7dc258"),
		MAGENTA_4(NumberSet.MAGENTA, 4, "2850327173de9782fffd37442cebf478d6cdbe671731421fbad0dc2b35d588a8"),
		MAGENTA_5(NumberSet.MAGENTA, 5, "58787fdc93ea2a7251b13e29823227ee4e2915a8ba6d399ea8dd19e5da87c5e"),
		MAGENTA_6(NumberSet.MAGENTA, 6, "83e8a2c8c3f5ea4ae1599e0c17052c5443e736a233b4ee3c9641f3b9cc9d"),
		MAGENTA_7(NumberSet.MAGENTA, 7, "68c9849a03d54b12ef9a4c6f92946ed94d4b2a884eec2b4289ed1dd7cb7128"),
		MAGENTA_8(NumberSet.MAGENTA, 8, "4badf86af5a6797fc5f8d340ed914ec042881610a5e8b8f9c4f0f4cdd722"),
		MAGENTA_9(NumberSet.MAGENTA, 9, "b96727df2d4c14b1b367e0376d8ad2c11953673a1647a3196a51fd4322a4"),
		
		ORANGE_0(NumberSet.ORANGE, 0, "5bfa63a0a5428b273453ffe784d3e489cbcf6d12b78450a3515716ce724f4"),
		ORANGE_1(NumberSet.ORANGE, 1, "bcfaffa8c6c7f62621682fe56711dc3b894465fdf7a62f43b31a0d3403f34e7"),
		ORANGE_2(NumberSet.ORANGE, 2, "7410db623735c146c7c487e3692d1c5b5e23bf691ef0665c2e9549479d828f"),
		ORANGE_3(NumberSet.ORANGE, 3, "ef8e7acf7242afa586c3d095f87fe9de7b7ca24c4a28a56040347c657960e36"),
		ORANGE_4(NumberSet.ORANGE, 4, "947c73d7d2ef14712ae59c57d64e512a7f9c527746b8bc424270f9e37c181c78"),
		ORANGE_5(NumberSet.ORANGE, 5, "75aff92de892d5922ad753dad5ae3479ab150af4ed284abfa75ca7a995c1893"),
		ORANGE_6(NumberSet.ORANGE, 6, "f16d423c7fd64c5c7f408546da4b787e93dbeac0e77b19c49b9ad4ff418f2d1"),
		ORANGE_7(NumberSet.ORANGE, 7, "7487e912a8e9bfc4912436af56c46c2e6a15a1d22d1ad18d46a229d7648e"),
		ORANGE_8(NumberSet.ORANGE, 8, "1101d4b47dccb7612ac5efde5ae2441c82c3f0a684041aed38276dbdf94"),
		ORANGE_9(NumberSet.ORANGE, 9, "ba46857441caae6e16c9296fb571482aa5136268d39e35b7acfbf5139a37e03d"),
		
		PINK_0(NumberSet.PINK, 0, "6799351fd521fcc53732c6cf114b36f1bf9688c944b9622cc1f320dbee328f89"),
		PINK_1(NumberSet.PINK, 1, "5ba2cd9ef956eb2c32db847b9d3926a5ddc64370a7ac34e3f08cf96a88e4"),
		PINK_2(NumberSet.PINK, 2, "4f7f9df399389611a3b331419b88f153e5ec1509ba447d82f0252b566492"),
		PINK_3(NumberSet.PINK, 3, "d37b2a657719da61bc80669217daae1b4fd31416c1a4e968d3111a888ae3584"),
		PINK_4(NumberSet.PINK, 4, "53341ff75ff9f7872153796b79457c162ddc3d249ff872cdade75460491f"),
		PINK_5(NumberSet.PINK, 5, "3ff87edb982d7372acde92c4524a2245571f5aa1ae940d817bffbc2a288"),
		PINK_6(NumberSet.PINK, 6, "ad617cc9bf983333bf12664143a772d52156ac6c48e168a1d916fb629916fb"),
		PINK_7(NumberSet.PINK, 7, "36a3a8ee54aa8730c9229aa04e32600b32e753f2a6cc56411c3b10d45d8e2e"),
		PINK_8(NumberSet.PINK, 8, "1e461fe6ebcddba14f90623486968ba9c7656d53d1aa631a574625a096917e0"),
		PINK_9(NumberSet.PINK, 9, "1aede60554a3c769189d1660761aa5ae82d84f5b66652229b66a34f4dd33c"),
		
		PURPLE_0(NumberSet.PURPLE, 0, "dcdf22e25ab547b376f083cbf8462c4268cef5555a9689b0e7d2dffe8672b2"),
		PURPLE_1(NumberSet.PURPLE, 1, "78a42df06fc916de110f61bd76eddbf58ed4249fce5ee51c219ec75a37b414"),
		PURPLE_2(NumberSet.PURPLE, 2, "1ef134f0efa88351b837f7c087afe1b3fb36435ab7d746fa37c0ef155e4f29"),
		PURPLE_3(NumberSet.PURPLE, 3, "1965e9c57c14c95c84e622e5306e1cf23bc5f1e47ac791f3d357b5ae8cded24"),
		PURPLE_4(NumberSet.PURPLE, 4, "6e31837d89c264c3b54f8214ae24f89a368a92bc46df9225333ad7cd449f856"),
		PURPLE_5(NumberSet.PURPLE, 5, "49357cb4664426a9ae0f9c7725ea4351dc69f15a8062c03591e26ac11bbc5a"),
		PURPLE_6(NumberSet.PURPLE, 6, "b8d550717d26ae8dda55e9a0a9edc92f04a774b501a36e24c8c4e682e32c59"),
		PURPLE_7(NumberSet.PURPLE, 7, "89141952aabca4987bddf208c5e15ca6a5bfce239af39584aeb5ee876d87"),
		PURPLE_8(NumberSet.PURPLE, 8, "a112a392ef19ee393a14505bbdad9c1e9293a04ec3ab337e374880222e708244"),
		PURPLE_9(NumberSet.PURPLE, 9, "b7277b841ef2f06e6b7631d9cfac275498f889fad94687668b251fc339a51f0"),
		
		RED_0(NumberSet.RED, 0, "85bd1e613ff32b523ccf9e574cc311b798c2b3a6828f0f71a254c995e6db8e5"),
		RED_1(NumberSet.RED, 1, "8d2454e4c67b323d5be953b5b3d54174aa271460374ee28410c5aeae2c11f5"),
		RED_2(NumberSet.RED, 2, "b13b778c6e5128024214f859b4fadc7738c7be367ee4b9b8dbad7954cff3a"),
		RED_3(NumberSet.RED, 3, "031f66be0950588598feeea7e6c6779355e57cc6de8b91a44391b2e9fd72"),
		RED_4(NumberSet.RED, 4, "95bc42c69846c3da9531ac7dba2b55363f8f9472576e17d423b7a9b81c9151"),
		RED_5(NumberSet.RED, 5, "df3f565a88928ee5a9d6843d982d78eae6b41d9077f2a1e526af867d78fb"),
		RED_6(NumberSet.RED, 6, "c5da1cb6c4c23710224b4f4e8d6ffcf8b4b55f7fe891c1204af7485cf252a1d8"),
		RED_7(NumberSet.RED, 7, "af4e7a5cf5b5a4d2ff4fb0433b1a68751aa12e9a021d3918e92e219a953b"),
		RED_8(NumberSet.RED, 8, "1683440c6447c195aaf764e27a1259219e91c6d8ab6bd89a11ca8d2cc799fa8"),
		RED_9(NumberSet.RED, 9, "f8977adedfa6c81a67f825ea37c4d5aa90dfe3c2a72dd98791f4521e1da36"),
		
		WHITE_0(NumberSet.WHITE, 0, "3f09018f46f349e553446946a38649fcfcf9fdfd62916aec33ebca96bb21b5"),
		WHITE_1(NumberSet.WHITE, 1, "ca516fbae16058f251aef9a68d3078549f48f6d5b683f19cf5a1745217d72cc"),
		WHITE_2(NumberSet.WHITE, 2, "4698add39cf9e4ea92d42fadefdec3be8a7dafa11fb359de752e9f54aecedc9a"),
		WHITE_3(NumberSet.WHITE, 3, "fd9e4cd5e1b9f3c8d6ca5a1bf45d86edd1d51e535dbf855fe9d2f5d4cffcd2"),
		WHITE_4(NumberSet.WHITE, 4, "f2a3d53898141c58d5acbcfc87469a87d48c5c1fc82fb4e72f7015a3648058"),
		WHITE_5(NumberSet.WHITE, 5, "d1fe36c4104247c87ebfd358ae6ca7809b61affd6245fa984069275d1cba763"),
		WHITE_6(NumberSet.WHITE, 6, "3ab4da2358b7b0e8980d03bdb64399efb4418763aaf89afb0434535637f0a1"),
		WHITE_7(NumberSet.WHITE, 7, "297712ba32496c9e82b20cc7d16e168b035b6f89f3df014324e4d7c365db3fb"),
		WHITE_8(NumberSet.WHITE, 8, "abc0fda9fa1d9847a3b146454ad6737ad1be48bdaa94324426eca0918512d"),
		WHITE_9(NumberSet.WHITE, 9, "d6abc61dcaefbd52d9689c0697c24c7ec4bc1afb56b8b3755e6154b24a5d8ba"),
		
		YELLOW_0(NumberSet.YELLOW, 0, "15b2484ad3693c69110ca45e28201771605bbeb49f277382ef2d9921455"),
		YELLOW_1(NumberSet.YELLOW, 1, "b949df36a1a3f7cb4c67065a9d5350258a3c51b02a1a377b84a82876d77b"),
		YELLOW_2(NumberSet.YELLOW, 2, "58cca1e0fb5d9bf77f527356e8bf4e53cb4a4c56f11e779abadae541bbedc6"),
		YELLOW_3(NumberSet.YELLOW, 3, "fe907672a470938525058c0ea319d8be5ba28d972c52f2bd55dbf4182b8"),
		YELLOW_4(NumberSet.YELLOW, 4, "af2d2601f9c1aceffcc823657e2259d358bb52bd46312bf1c76ee23d3b17"),
		YELLOW_5(NumberSet.YELLOW, 5, "b7973537bd2162a78a586397a1eda8599f4c90dc11a1e7d655288483429251bd"),
		YELLOW_6(NumberSet.YELLOW, 6, "df4960f57dcc1852f974a4288778b45477678b3c3671e2cfa8205022443b6923"),
		YELLOW_7(NumberSet.YELLOW, 7, "e4fd8cfc44658f6c78c376b2752df3c63610a5aed194f2eca3e6736d47ce72"),
		YELLOW_8(NumberSet.YELLOW, 8, "7acc81bd5a59a1862a2c01be7412231e15ce057d458fbc18435b9385593f5"),
		YELLOW_9(NumberSet.YELLOW, 9, "85bdbddbb81ae7a894b6df0a5f8e21c22fbd065dc8211b41737e0a1d0c2cfc6"),
		
		BLUE_0(NumberSet.BLUE, 0, "e5a74393822151d3e81f8794ff84bc1b98c57ee9a8a81e29831f73eda2b36a"),
		BLUE_1(NumberSet.BLUE, 1, "bd21b0bafb89721cac494ff2ef52a54a18339858e4dca99a413c42d9f88e0f6"),
		BLUE_2(NumberSet.BLUE, 2, "2b3513aa4117a3a329e1f9a43d2a8c51cd722aadd4e8af2feda67b33b64c298"),
		BLUE_3(NumberSet.BLUE, 3, "69e38c81436f3da120672efb162d2f4ea874ab0ce545ae323777f5e573c254a"),
		BLUE_4(NumberSet.BLUE, 4, "f03d45521c27fdd2f2b1139a1a17d6495e8f47d9f123493d4dd8aa06aff40ce"),
		BLUE_5(NumberSet.BLUE, 5, "9f2a14dbf9588126c43cd2111eb41f1de6d8c281b6519194364b9965fc456e"),
		BLUE_6(NumberSet.BLUE, 6, "eae7cb37ffa6866317672924301b1b29633e6f23f2525513dbf729bd2d066"),
		BLUE_7(NumberSet.BLUE, 7, "e21d562c5a51b64229fc626f25c421f6ac38cf7839e1c6c47168fac3742ccdf8"),
		BLUE_8(NumberSet.BLUE, 8, "b6b56cb0b48d9c9edd1984afe1713b98eea96d29be5b2b58da4444a1018e95d"),
		BLUE_9(NumberSet.BLUE, 9, "137fdef8295df3fb6dea21c6c8e451f1f7fd657a61cc6423a3ce42c2fb961b6c"),
		
		BLACK_0(NumberSet.BLACK, 0, "6d68343bd0b129de93cc8d3bba3b97a2faa7ade38d8a6e2b864cd868cfab"),
		BLACK_1(NumberSet.BLACK, 1, "d2a6f0e84daefc8b21aa99415b16ed5fdaa6d8dc0c3cd591f49ca832b575"),
		BLACK_2(NumberSet.BLACK, 2, "96fab991d083993cb83e4bcf44a0b6cefac647d4189ee9cb823e9cc1571e38"),
		BLACK_3(NumberSet.BLACK, 3, "cd319b9343f17a35636bcbc26b819625a9333de3736111f2e932827c8e749"),
		BLACK_4(NumberSet.BLACK, 4, "d198d56216156114265973c258f57fc79d246bb65e3c77bbe8312ee35db6"),
		BLACK_5(NumberSet.BLACK, 5, "7fb91bb97749d6a6eed4449d23aea284dc4de6c3818eea5c7e149ddda6f7c9"),
		BLACK_6(NumberSet.BLACK, 6, "9c613f80a554918c7ab2cd4a278752f151412a44a73d7a286d61d45be4eaae1"),
		BLACK_7(NumberSet.BLACK, 7, "9e198fd831cb61f3927f21cf8a7463af5ea3c7e43bd3e8ec7d2948631cce879"),
		BLACK_8(NumberSet.BLACK, 8, "84ad12c2f21a1972f3d2f381ed05a6cc088489fcfdf68a713b387482fe91e2"),
		BLACK_9(NumberSet.BLACK, 9, "9f7aa0d97983cd67dfb67b7d9d9c641bc9aa34d96632f372d26fee19f71f8b7");
		
		private NumberSet set;
		private int number;
		private String minecraftSkinUrl;
		
		private NumberSkulls(NumberSet set, int number, String minecraftSkinUrl) {
			this.set = set;
			this.number = number;
			this.minecraftSkinUrl = minecraftSkinUrl;
		}
		
		public NumberSet getNumberSet() {
			return set;
		}
		
		public int getNumber() {
			return number;
		}
		
		public GameProfile getGameProfile() {
			return SkullFactory.getFakeGameProfileFromMinecraftSkinUrl("http://textures.minecraft.net/texture/" + this.minecraftSkinUrl);
		}
		
		public static Optional<NumberSkulls> getNumberSkulls(NumberSet set, int number) {
			return Arrays.stream(NumberSkulls.values()).filter(ns -> ns.getNumberSet().equals(set) && ns.getNumber() == number).findFirst();
		}
		
	}
	
	public enum NumberSet {
		
		BROWN,
		CYAN,
		GRAY,
		GREEN,
		LIGHT_BLUE,
		LIGHT_GRAY,
		LIME,
		MAGENTA,
		ORANGE,
		PINK,
		PURPLE,
		YELLOW,
		BLUE,
		BLACK,
		WHITE,
		RED;
		
	}
	
}
