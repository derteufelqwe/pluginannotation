#1 AutoPluginYML

#2 Where to find which values

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
| MCAPIVersion | MCPlugin |
| MCAuthor | MCPlugin |
| MCDepend | MCPlugin |
| MCLoad | MCPlugin |
| MCLoadBefore | MCPlugin |
| MCSoftDepend | MCPlugin |
| MCCommand | CommandExecutor |
| MCListener | Listener |
| MCTabComplete | TabCompleter |
| MCPermission | Anywhere |

# Template
The AutoPluginProcessor will detect if you have a plugin.yml in your resources folder
and uses this as a startingpoint. Annotations will have a higher priority and will
overrwrite values from the plugin.yml.

#3 Commands
Commands can be registered automaticly

#4 Listeners
Listeners can be registered automaticly

#5 Permissions
Permissions don't support inheritance