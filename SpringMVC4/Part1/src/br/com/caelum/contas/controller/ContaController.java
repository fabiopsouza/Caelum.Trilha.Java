package br.com.caelum.contas.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.caelum.contas.dao.ContaDAO;
import br.com.caelum.contas.modelo.Conta;

@Controller
public class ContaController {

	private ContaDAO dao;
	
	@Autowired
	public ContaController(ContaDAO dao) {
		this.dao = dao;
	}
	
	@RequestMapping("/form")
	public String formulario(){
		return "conta/formulario";
	}
	
	@RequestMapping("/adicionaConta")
	public String adiciona(@Valid Conta conta, BindingResult result){
		
		if(result.hasErrors()){
			return "conta/formulario";
		}
		
		System.out.println("Conta adicionada �: " + conta.getDescricao());
		dao.adiciona(conta);

		return "conta/conta-adicionada";
	}
	
	@RequestMapping("/removeConta")
	public String remove(Conta conta){
		dao.remove(conta);
		return "redirect:listaContas";
	}
	
	@RequestMapping("/pagaConta")
	public void paga(Conta conta, HttpServletResponse response){
		dao.paga(conta.getId());
		
		response.setStatus(200);
	}
	
	@RequestMapping("/listaContas")
	public ModelAndView lista(){
		List<Conta> contas = dao.lista();
		
		ModelAndView mv = new ModelAndView("conta/lista");
		mv.addObject("todasContas", contas);
		
		return mv;
	}
}
