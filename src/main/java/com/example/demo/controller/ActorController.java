package com.example.demo.controller;

import com.example.demo.dao.ActorDao;
import com.example.demo.model.Actor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActorController {
    private final ActorDao actorDao;

    public ActorController(ActorDao actorDao) {
        this.actorDao = actorDao;
    }

    @GetMapping("actor")
    public List<Actor> getAllActors() {
        return this.actorDao.findAll();
    }

    @GetMapping("actor/id/{id}")
    public Actor getActorById(@PathVariable int id) {
        return this.actorDao.findById(id);
    }

    @GetMapping("actor/name/{name}")
    public Actor getActorById(@PathVariable String name) {
        return this.actorDao.findByFirstName(name);
    }

    @DeleteMapping("actor/delete/{id}")
    public ResponseEntity<String> deleteActorById(@PathVariable int id) {
        boolean deleted = this.actorDao.delete(id);
        if (deleted) return ResponseEntity.ok("Actor deleted successfully.");
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Actor not found.");
    }

    @PostMapping("actor/save")
    public ResponseEntity<String> saveActor(@RequestBody Actor actor) {
        if (this.actorDao.save(actor) != null) return ResponseEntity.ok("Actor saved successfully.");
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while saving actor.");
    }

    @PatchMapping("actor/update/{id}")
    public ResponseEntity<String> updateActor(@PathVariable int id, @RequestBody Actor actor) {
        boolean updated = this.actorDao.update(id, actor);
        if (updated) return ResponseEntity.ok("Actor updated successfully.");
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while updating actor.");
    }

}
