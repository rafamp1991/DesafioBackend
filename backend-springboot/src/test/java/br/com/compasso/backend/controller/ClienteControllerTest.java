package br.com.compasso.backend.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
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

import br.com.compasso.backend.model.CidadeModel;
import br.com.compasso.backend.model.ClienteModel;
import br.com.compasso.backend.repository.CidadeRepository;
import br.com.compasso.backend.repository.ClienteRepository;

@WebMvcTest(ClienteController.class)
@ActiveProfiles("test")
public class ClienteControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockBean
    private ClienteRepository clienteRepository;
	
	@MockBean
    private CidadeRepository cidadeRepository;
	
	private ClienteModel clienteModel;
	
//	@Before
//    public void setUP() {
//		
//        clienteModel = new ClienteModel();
//        clienteModel.setClienteId(1L);
//        clienteModel.setNome("Rafael");
//        clienteModel.setSobrenome("Martins de Padua"); 
//        clienteModel.setSexo("masculino");
//        clienteModel.setDataNascimento(LocalDate.of(1991, 02, 06));
//        clienteModel.setIdade(29);
//    }
	
	@Test
	public void getClientesModels() throws Exception {
		List<ClienteModel> listaClientes = new ArrayList<ClienteModel>();
		listaClientes.add(clienteModel);
		
	    Mockito.doReturn(listaClientes)
		.when(clienteRepository)
		.findAll();
	    mockMvc.perform(MockMvcRequestBuilders.get("/clientes"))
	            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
	}
	
	@Test
	public void getByNome() throws Exception {
		List<ClienteModel> listaClientes = new ArrayList<ClienteModel>();
		listaClientes.add(clienteModel);
		
	    Mockito.doReturn(listaClientes)
		.when(clienteRepository)
		.findByNome("Rafael");
	    mockMvc.perform(MockMvcRequestBuilders.get("/clienteNome/Rafael"))
	            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
	}
	
	@Test
	public void GetById() throws Exception {
	
		clienteModel = new ClienteModel();
        clienteModel.setClienteId(1L);
        clienteModel.setNome("Rafael");
        clienteModel.setSobrenome("Martins de Padua"); 
        clienteModel.setSexo("masculino");
        clienteModel.setDataNascimento(LocalDate.of(1991, 02, 06));
        clienteModel.setIdade(29);
		
	    Mockito.doReturn(Optional.of(clienteModel))
		.when(clienteRepository)
		.findById(1L);		
	    mockMvc.perform(MockMvcRequestBuilders.get("/clienteId/1"))
	            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
	}
	
	@Test
    public void clienteCreate() throws Exception {
		
		clienteModel = new ClienteModel();
        clienteModel.setClienteId(1L);
        clienteModel.setNome("Rafael");
        clienteModel.setSobrenome("Martins de Padua"); 
        clienteModel.setSexo("masculino");
        clienteModel.setDataNascimento(LocalDate.of(1991, 02, 06));
        clienteModel.setIdade(29);
		
        mockMvc.perform(MockMvcRequestBuilders.post("/cliente")
        		.contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(clienteModel)))
	    		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
    }
	
	@Test
    public void clienteUpdate() throws Exception {
		
		ClienteModel atualizaCliente = new ClienteModel();
		atualizaCliente.setClienteId(1L);
		atualizaCliente.setNome("Pedro");
		atualizaCliente.setSobrenome("Ramos");
		atualizaCliente.setSexo("masculino");
		atualizaCliente.setDataNascimento(LocalDate.of(2002, 01, 18));
		atualizaCliente.setIdade(18);
		atualizaCliente.getCidadeModel().setCidadeId(1500107L);
		
	    when(clienteRepository.findById(1L)).thenReturn(Optional.of(atualizaCliente));
	    when(clienteRepository.save(any(ClienteModel.class))).thenReturn(atualizaCliente);
        
	    mockMvc.perform(MockMvcRequestBuilders.put("/cliente/1")
	    		.contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(atualizaCliente)))
	    		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
	            .andExpect(jsonPath("$.clienteId", is(1)))
	            .andExpect(jsonPath("$.nome", is("Pedro")))
	            .andExpect(jsonPath("$.sobrenome", is("Ramos")))
	            .andExpect(jsonPath("$.sexo", is("masculino")))
	            .andExpect(jsonPath("$.datanascimento", is(LocalDate.of(2002, 01, 18))))
	            .andExpect(jsonPath("$.idade", is(18)));
    }
	
	@Test
    public void clienteDelete() throws Exception {
		
		clienteModel = new ClienteModel();
        clienteModel.setClienteId(1L);
        clienteModel.setNome("Rafael");
        clienteModel.setSobrenome("Martins de Padua"); 
        clienteModel.setSexo("masculino");
        clienteModel.setDataNascimento(LocalDate.of(1991, 02, 06));
        clienteModel.setIdade(29);
		
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteModel));
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/cliente/1"))
	    		.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        
        verify(clienteRepository, times(1)).deleteById(1L);
    }
}
