package com.seap.gestao.visitas.controller;

import com.seap.gestao.visitas.dto.VisitaDTO;
import com.seap.gestao.visitas.dto.VisitaResponseDTO;
import com.seap.gestao.visitas.model.Visita;
import com.seap.gestao.visitas.service.VisitaService;
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
@RequestMapping("/api/visitas")
@Tag(name = "Visitas", description = "Endpoints para gerenciamento de visitas")
@SecurityRequirement(name = "bearer-key")
public class VisitaController {

    private final VisitaService visitaService;

    @Autowired
    public VisitaController(VisitaService visitaService) {
        this.visitaService = visitaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as visitas")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<List<VisitaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(visitaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar visita por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE', 'VISITANTE')")
    public ResponseEntity<VisitaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(visitaService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Agendar nova visita")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<VisitaResponseDTO> agendar(@Valid @RequestBody VisitaDTO visitaDTO) {
        return new ResponseEntity<>(visitaService.agendar(visitaDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status da visita")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<VisitaResponseDTO> alterarStatus(
            @PathVariable Long id,
            @RequestParam Visita.StatusVisita status) {
        return ResponseEntity.ok(visitaService.alterarStatus(id, status));
    }

    @GetMapping("/detento/{detentoId}")
    @Operation(summary = "Listar visitas por detento")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<List<VisitaResponseDTO>> buscarPorDetento(@PathVariable Long detentoId) {
        return ResponseEntity.ok(visitaService.buscarPorDetento(detentoId));
    }

    @GetMapping("/visitante/{visitanteId}")
    @Operation(summary = "Listar visitas por visitante")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE', 'VISITANTE')")
    public ResponseEntity<List<VisitaResponseDTO>> buscarPorVisitante(@PathVariable Long visitanteId) {
        return ResponseEntity.ok(visitaService.buscarPorVisitante(visitanteId));
    }
}
