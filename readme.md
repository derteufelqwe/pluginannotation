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