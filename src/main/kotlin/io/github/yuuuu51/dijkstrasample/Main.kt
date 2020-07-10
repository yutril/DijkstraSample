package io.github.yuuuu51.dijkstrasample

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import io.github.yuuuu51.dijkstralib.DijkstraLib
import io.github.yuuuu51.dijkstralib.element.Edge
import io.github.yuuuu51.dijkstralib.element.ElementFactory
import io.github.yuuuu51.dijkstralib.element.Node

const val START_NODE_ID = "1"
const val GOAL_NODE_ID = "8"

fun main(args: Array<String>) {
    val edges = mutableMapOf<String, Edge>()
    loadJson("/edges.json").forEach {(id, length) ->
        check(length is Double)
        edges[id] = ElementFactory.createEdge(id, length)
    }
    val nodes = mutableMapOf<String, Node>()
    loadJson("/nodes.json").forEach {(id, data) ->
        check(data is List<*>)
        val node = ElementFactory.createNode(id)
        data.forEach {
            check(it is String)
            edges[it]?.let { it1 -> node.addEdge(it1) }
        }
        nodes[id] = node
    }
    val dijkstra = DijkstraLib(nodes, edges)
    dijkstra.execute(START_NODE_ID, GOAL_NODE_ID).forEach {
        println("Node:${it.id}")
    }
}

fun loadJson(path: String): JsonObject {
    val klaxon = Klaxon()
    val reader = klaxon::class.java.getResourceAsStream(path).bufferedReader()
    return klaxon.parseJsonObject(reader)
}