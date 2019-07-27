# RedstoneCore

## Description
RedstoneCore is a library which brings a ton of useful features to the table. It brings object oriented GUI, with buttons, sliders, switches, selectors and more.
The GUI library also allows for an infinite quantity of popups over the current interface and it can remember your original interface and bring you back to it (Useful for e.g A confirmation dialog).

The library of course dos not stop there, it brings to the table a plethora of utilities related to inventories, items, and skulls as well as some basic Reflection methods.

There's also a small range of custom events implemented (currently one):
- PlayerJumpEvent

## Usage
To use RedstoneCore in your plugin, hop over to the [release section](https://github.com/RedstoneTek/RedstoneCore/releases) and grab the latest release.
Add this release (.jar) to your **plugins folder** and add it to your project's build path.

**Alternatively,** if you prefer to use Maven, you can use JitPack to import RedstoneCore.

Add the [JitPack](https://jitpack.io/) repository to your pom.xml
```
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

and add the RedstoneCore dependency to your dependencies
```
<dependency>
  <groupId>com.tek</groupId>
  <artifactId>rcore</artifactId>
  <version>VERSION TAG</version>
</dependency>
```

## License
This project is licensed under the GNU General Public License.