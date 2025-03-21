package com.seap.gestao.visitas.controller;

import com.seap.gestao.visitas.dto.DetentoDTO;
import com.seap.gestao.visitas.service.DetentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detentos")
@Tag(name = "Detentos", description = "Endpoints para gerenciamento de detentos")
@SecurityRequirement(name = "bearer-key")
public class DetentoController {

    private final DetentoService detentoService;

    @Autowired
    public DetentoController(DetentoService detentoService) {
        this.detentoService = detentoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os detentos")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<List<DetentoDTO>> listarTodos() {
        return ResponseEntity.ok(detentoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar detento por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<DetentoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(detentoService.buscarPorId(id));
    }

    @GetMapping("/matricula/{matricula}")
    @Operation(summary = "Buscar detento por matrícula")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<DetentoDTO> buscarPorMatricula(@PathVariable String matricula) {
        return ResponseEntity.ok(detentoService.buscarPorMatricula(matricula));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo detento")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DetentoDTO> criar(@Valid @RequestBody DetentoDTO detentoDTO) {
        return new ResponseEntity<>(detentoService.criar(detentoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar detento")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DetentoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody DetentoDTO detentoDTO) {
        return ResponseEntity.ok(detentoService.atualizar(id, detentoDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir detento")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        detentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status do detento")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DetentoDTO> alterarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        return ResponseEntity.ok(detentoService.alterarStatus(id, ativo));
    }
}
