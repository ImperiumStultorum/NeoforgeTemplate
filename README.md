# Imperium Neoforge Template

A fork of [Erdragh's Neoforge Kotlin MDK](https://github.com/Erdragh/Neoforge-Kotlin-MDK) that's tailored more to our preferences.

A vague list of changes follows:
- Switch to using Gradle's kotlin DSL, roughly using [ColdStuff1's MCMod](https://github.com/Coldstuff1/MCMod) as a guide
- Change the namespace of the example mod to `com.stultorum.mods.template` since nearly all mods by Imperium should use this namespace pattern
- Remove most of the comments that I feel are redundant (and changing them to more concise reminders where I feel they aren't)
- Updating the referenced Neoforge version and changing supported minecraft versions to `[1.21,1.21.2)`
- Add `TODO`s where changes are known to be required
- Add logic to publish to Imperium's maven
- Set default license to LGPL-3.0 as most Imperium mods use it
- Remove most of `ExampleMod`'s code
- A few other opinionated changes

It is worth noting that normally I would release a template as CC0 or Unlicense but since upstream is MIT I'm not sure that's possible. As such I'm simply going to keep upstreams license.
If you are aware of problems with keeping upstream license or if it's possible to use one of the aforementioned licenses, do let me know.