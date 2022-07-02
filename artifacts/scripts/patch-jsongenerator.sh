#!/bin/bash

cd ${HOME}
cp -r patches/jsongenerator.patch jsongenerator/.
cd jsongenerator

# Convert line endings to ensure patches are applied properly
dos2unix jsongenerator.patch

# Apply Patches
git apply jsongenerator.patch
