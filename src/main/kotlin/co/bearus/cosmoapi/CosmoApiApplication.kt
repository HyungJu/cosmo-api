package co.bearus.cosmoapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CosmoApiApplication

fun main(args: Array<String>) {
	runApplication<CosmoApiApplication>(*args)
}
