# AutoPluginYML
AutoPluginYML is an annotation processor which mainly creates the plugin.yml for you as well as 
removes some boilerplate code like registering commands and listeners in Java.
It also checks if inputs like your plugin name match spigots name requirements and throw errors
at compile time if you use something incorrectly.

AutoPluginTestPlugin is an example project using AutoPluginYML.

# Where to find which values

| Value        | Annotation |
| ------------- |:-------------|
| main      | MCPlugin |
| name      | MCPlugin |
| version | MCPlugin |
| description | MCPlugin |
| api-version | MCAPIVersion |
| load | MCLoad |
| author | MCAuthor |
| authors | MCAuthor |
| website | MCAuthor |
| depend | MCDepend |
| prefix | MCPlugin | 
| softdepend | MCSoftdepend |
| loadbefore | MCLoadBefore |
| commands | MCCommands |
| permissions | MCPermissions |

| Annotation        | Can be applied to |
| ------------- |:-------------|
| MCPlugin | JavaPlugin |
| MCAPIVersion | @MCPlugin |
| MCAuthor | @MCPlugin |
| MCDepend | @MCPlugin |
| MCLoad | @MCPlugin |
| MCLoadBefore | @MCPlugin |
| MCSoftDepend | @MCPlugin |
| MCCommand | CommandExecutor |
| MCListener | Listener |
| MCTabComplete | TabCompleter |
| MCPermission | Anywhere |
| MCDontIgnore | Anywhere |

# Template
The AutoPluginProcessor will detect if you have a `plugin_T.yml` (_T = Template) in your resources folder
and uses this as a starting point. Annotations have a higher priority and will
overwrite values from the `plugin_T.yml`.

# Usage
Simply add (maven link here) to your project.

Annotate your main class with `@MCPlugin` and your are good to go. Other annotations and where to put 
them to generate what can be taken from the two tables above.
To automatically register your commands and listeners simply add `new AutoRegister.generate(this)` to 
your `onEnable()` method and import `de.derteufelqwe.AutoPlugin.AutoRegister`. 
Your IDE will tell you that this class is not found. This is because it's getting generated at 
compile time, so just ignore the error.

To get rid if this error you need to mark the directory where the class is generated as a 
'generated sources root' so your IDE will find the class.

# Commands
Classes implementing `CommandExecutor` can be registered using the `@MCCommand` annotation.
Using the `new AutoRegister.generate(this)` will automatically register them in Java as well.

# Listeners
Classes implementing `Listener` can be registered using the `@MCListener` annotation.
Using the `new AutoRegister.generate(this)` will automatically register them in Java as well.

# @MCDontIgnore
`@MCDontIgnore` is an annotation used to tell the annotation processor that a class exists. 
Let's look at an example.

You have a class `Command1` which implements `CommandExecutor` and has the `@MCCommand` annotation.
If you compile the project the AP will see the class and register the command. Nice.
If you now remove the `@MCCommand` annotation and recompile the project the AP will not see
`Command1` as it doesn't contain valid annotations. Because it's invisible to the AP it won't update the
plugin.yml unless your compilation includes another `@MC...` annotation. To prevent errors and constant 
full recompilations you can add `@MCDontIgnore` to the class to tell the AP that there is a class.

# Boundaries
- You need to have at least one annotated class in your compilation, otherwise the Annotation
Processor won't get called. This means that you can't just change a value in your plugin.yml and 
build without any changes to your annotated classes. If there are no changes to your classes you 
can just rebuild the whole project or force rebuild one class.
- The `@MCPermission` annotation doesn't support inheritance. If you want to use inheritance you 
have to manually add them to your `plugin_T.yml`.
- The Command and Listener classes need to have a No-args constructor if you want to use them with
`@MCCommand` or `@MCListener`