package june.intrerface

import june.application.TaskService
import june.domain.Task
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping
    fun create(@RequestBody task: Task): ResponseEntity<String> {
        val id = taskService.create(task)
        return ResponseEntity.ok(id)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): ResponseEntity<Task> {
        val task = taskService.findById(id)
        return task?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @PatchMapping("/{id}/done")
    fun markDone(@PathVariable id: String): ResponseEntity<Void> {
        return if (taskService.markDone(id)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Void> {
        taskService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun list(): List<Pair<String, Task>> = taskService.findAll()
}
