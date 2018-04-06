#!/bin/bash

cd console/
npm install
npm run build.prod -- --scss
cp -fr dist/prod/* ../src/resources/console/
cd ../src/resources
sed -i -- 's/=\"\/css\//=\"css\//g' console/index.html
sed -i -- 's/=\"\/js\//=\"js\//g' console/index.html