This is an experiment.

It connects to FurAffinity website, logins with the specified credentials then retrieves an user favorites list (it may be any user, as long as the faves are public)
It will then grab the tags from every image and, when done, generate a frequency list of the keywords found.
At the moment stopwords (a, and, the, or, ...) are skipped and keywords are lowercased.

The keyword is outputed to a csv file "fa-tags.csv" in the working directory. (format : keyword;frequency)

This projects uses JDK8 and gradle.
To create an executable jar, run gradlew shadowJar
