package com.seap.gestao.visitas.controller;

import com.seap.gestao.visitas.dto.VisitanteDTO;
import com.seap.gestao.visitas.model.Visitante;
import com.seap.gestao.visitas.service.VisitanteService;
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
@RequestMapping("/api/visitantes")
@Tag(name = "Visitantes", description = "Endpoints para gerenciamento de visitantes")
@SecurityRequirement(name = "bearer-key")
public class VisitanteController {

    private final VisitanteService visitanteService;

    @Autowired
    public VisitanteController(VisitanteService visitanteService) {
        this.visitanteService = visitanteService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os visitantes")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<List<VisitanteDTO>> listarTodos() {
        return ResponseEntity.ok(visitanteService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar visitante por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<VisitanteDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(visitanteService.buscarPorId(id));
    }

    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar visitante por CPF")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<VisitanteDTO> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(visitanteService.buscarPorCpf(cpf));
    }

    @GetMapping("/rg/{rg}")
    @Operation(summary = "Buscar visitante por RG")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<VisitanteDTO> buscarPorRg(@PathVariable String rg) {
        return ResponseEntity.ok(visitanteService.buscarPorRg(rg));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo visitante")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VisitanteDTO> criar(@Valid @RequestBody VisitanteDTO visitanteDTO) {
        return new ResponseEntity<>(visitanteService.criar(visitanteDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar visitante")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VisitanteDTO> atualizar(@PathVariable Long id, @Valid @RequestBody VisitanteDTO visitanteDTO) {
        return ResponseEntity.ok(visitanteService.atualizar(id, visitanteDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir visitante")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        visitanteService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status do visitante")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VisitanteDTO> alterarStatus(
            @PathVariable Long id,
            @RequestParam Visitante.StatusVisitante status) {
        return ResponseEntity.ok(visitanteService.alterarStatus(id, status));
    }
}
