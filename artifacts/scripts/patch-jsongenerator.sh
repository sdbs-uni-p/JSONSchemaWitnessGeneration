#!/bin/bash

cp -r jsongenerator-patches/src/main/java/massiveTesting/ jsongenerator/src/main/java/
patch jsongenerator/build.gradle jsongenerator-patches/build.gradle.patch
patch jsongenerator/src/main/java/net/jimblackler/jsongenerator/Fixer.java jsongenerator-patches/Fixer.java.patch
patch jsongenerator/src/test/java/net/jimblackler/jsongenerator/Test.java jsongenerator-patches/Test.java.patch