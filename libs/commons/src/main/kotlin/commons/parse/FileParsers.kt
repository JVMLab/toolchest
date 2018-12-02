package com.jvmlab.commons.parse

import java.io.File

import com.jvmlab.commons.io.Yaml



fun File.parseYaml (yaml: Yaml): Parsed<File> = Parsed<File>(this, yaml::loadMap)