package com.jvmlab.commons.io

import java.io.File
import java.io.FileReader

import org.snakeyaml.engine.v1.api.*



class Yaml {
  private val load: Load

  init {
    val settings = LoadSettingsBuilder().build()
    load = Load(settings)
  }


  fun loadMap (file: File): Map<String, Any> {
    val reader = FileReader(file)
    return load.loadFromReader(reader) as Map<String, Any>
  }
}