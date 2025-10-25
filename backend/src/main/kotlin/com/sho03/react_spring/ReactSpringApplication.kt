package com.sho03.react_spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class ReactSpringApplication

fun main(args: Array<String>) {
	runApplication<ReactSpringApplication>(*args)
}
