package com.jvmlab.commons.io

import java.io.PrintWriter


fun filePrintWriterOrOut(name: String?) = name?.let {
  PrintWriter(it)
} ?: run {
  PrintWriter(System.out)
}