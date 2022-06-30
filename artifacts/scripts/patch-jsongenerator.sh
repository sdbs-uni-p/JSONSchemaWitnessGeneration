#!/bin/bash

cd ${HOME}
cp -r jsongenerator-patches/src/main/java/massiveTesting/ jsongenerator/src/main/java/

# Convert line endings to ensure patches are applied properly
dos2unix jsongenerator-patches/build.gradle.patch
dos2unix jsongenerator-patches/Fixer.java.patch
dos2unix jsongenerator-patches/Test.java.patch
dos2unix jsongenerator/build.gradle
dos2unix jsongenerator/src/main/java/net/jimblackler/jsongenerator/Fixer.java
dos2unix jsongenerator/src/test/java/net/jimblackler/jsongenerator/Test.java

# Apply Patches
patch jsongenerator/build.gradle jsongenerator-patches/build.gradle.patch
patch jsongenerator/src/main/java/net/jimblackler/jsongenerator/Fixer.java jsongenerator-patches/Fixer.java.patch
patch jsongenerator/src/test/java/net/jimblackler/jsongenerator/Test.java jsongenerator-patches/Test.java.patch