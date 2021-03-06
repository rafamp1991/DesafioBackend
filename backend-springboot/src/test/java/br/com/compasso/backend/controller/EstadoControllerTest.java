package br.com.compasso.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.ArgumentMatchers.any;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.compasso.backend.model.EstadoModel;
import br.com.compasso.backend.repository.CidadeRepository;
import br.com.compasso.backend.repository.EstadoRepository;

/**
 * @author Rafael Martins de Padua
 */
@WebMvcTest(EstadoController.class)
@ActiveProfiles("test")
public class EstadoControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockBean
    private EstadoRepository estadoRepository;
	
	@MockBean
    private CidadeRepository cidadeRepository;
	
	private EstadoModel estadoModel;
	
	/**
	 * Método para testar a Endpoint, 
	 * responsável por consultar todos os estados.
	 * @author Rafael Martins de Padua
	 * @throws Exception lança uma exceção em caso de erro
	 */
	@Test
	public void getEstadosModels() throws Exception {
		List<EstadoModel> listaEstados = new ArrayList<EstadoModel>();
		listaEstados.add(estadoModel);
		
	    Mockito.doReturn(listaEstados)
		.when(estadoRepository)
		.findAll();
	    mockMvc.perform(MockMvcRequestBuilders.get("/estados"))
	            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
	}
	
	/**
	 * Método para testar a Endpoint, 
	 * responsável por consultar um estado pelo nome.
	 * @author Rafael Martins de Padua
	 * @throws Exception lança uma exceção em caso de erro
	 */
	@Test
	public void GetByEstado() throws Exception {
		EstadoModel estado = new EstadoModel();
		
	    Mockito.doReturn(estado)
		.when(estadoRepository)
		.findByNome("Acre");
	    mockMvc.perform(MockMvcRequestBuilders.get("/estadoNome/Acre"))
	            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
	}
	
	/**
	 * Método para testar a Endpoint, 
	 * responsável por consultar um estado pelo UF.
	 * @author Rafael Martins de Padua
	 * @throws Exception lança uma exceção em caso de erro
	 */
	@Test
	public void GetByUf() throws Exception {
		EstadoModel estado = new EstadoModel();
		
	    Mockito.doReturn(estado)
		.when(estadoRepository)
		.findByUf("PA");
	    mockMvc.perform(MockMvcRequestBuilders.get("/estadoUf/PA"))
	            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
	}
	
	/**
	 * Método para testar a Endpoint, 
	 * responsável por cadastrar um novo estado.
	 * @author Rafael Martins de Padua
	 * @throws Exception lança uma exceção em caso de erro
	 */
	@Test
    public void estadoCreate() throws Exception {
		
		estadoModel = new EstadoModel();
        estadoModel.setEstadoId(12L);
        estadoModel.setNome("Acre");
        estadoModel.setUf("AC"); 
		
        mockMvc.perform(MockMvcRequestBuilders.post("/estado")
        		.contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(estadoModel)))
	    		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
    }
	
	/**
	 * Método para testar a Endpoint, 
	 * responsável por atualizar um estado.
	 * @author Rafael Martins de Padua
	 * @throws Exception lança uma exceção em caso de erro
	 */
	@Test
    public void estadoUpdate() throws Exception {
		
		EstadoModel atualizaEstado = new EstadoModel();
		atualizaEstado.setEstadoId(12L);
		atualizaEstado.setNome("Estado Teste");
		atualizaEstado.setUf("ET");
       
	    when(estadoRepository.findById(12L)).thenReturn(Optional.of(atualizaEstado));
	    when(estadoRepository.save(any(EstadoModel.class))).thenReturn(atualizaEstado);
        
	    mockMvc.perform(MockMvcRequestBuilders.put("/estado/12")
	    		.contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(atualizaEstado)))
	    		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
	            .andExpect(jsonPath("$.estadoId", is(12)))
	            .andExpect(jsonPath("$.nome", is("Estado Teste")))
	            .andExpect(jsonPath("$.uf", is("ET")));
    }
	
	/**
	 * Método para testar a Endpoint, 
	 * responsável por remover um estado.
	 * @author Rafael Martins de Padua
	 * @throws Exception lança uma exceção em caso de erro
	 */
	@Test
    public void estadoDelete() throws Exception {
		
		EstadoModel atualizaEstado = new EstadoModel();
		atualizaEstado.setEstadoId(12L);
		atualizaEstado.setNome("Estado Teste");
		atualizaEstado.setUf("ET");
		
		when(estadoRepository.findById(12L)).thenReturn(Optional.of(atualizaEstado));
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/estado/12"))
	    		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        
        verify(estadoRepository, times(1)).deleteById(12L);
    }
	
	/**
	 * Consulta um estado por um nome inexistente
	 * @author Rafael Martins de Padua
	 * @throws Exception lança uma exceção informando 
	 * 					 que o registro não pode ser encontrado.
	 */
	@Test
	public void estadoNaoExiste() throws Exception {
		
	    Mockito.doReturn(estadoModel)
		.when(estadoRepository)
		.findByNome("Texas");		
	    mockMvc.perform(MockMvcRequestBuilders.get("/estadoNome/Texas"))
	            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));
	}
}
