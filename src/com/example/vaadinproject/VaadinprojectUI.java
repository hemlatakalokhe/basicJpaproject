package com.example.vaadinproject;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Theme("vaadinproject")
public class VaadinprojectUI extends UI {
	private CustomerService service=CustomerService.getInstance();
	
	private Grid grid=new Grid();
	
	private CustomerForm form=new CustomerForm(this);
	private TextField filterText=new TextField();
	@Override
	protected void init(VaadinRequest request) 
	{
		 VerticalLayout layout=new VerticalLayout();
		 grid.setColumns("firstName","lastName","email","status");
		 
		 filterText.setInputPrompt("filter by name...");
		 filterText.addTextChangeListener(e ->{
			 grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, service.findAll(e.getText())));
		 });
		 
		 Button clearFilterTextBtn=new Button(FontAwesome.TIMES);
		
		 clearFilterTextBtn.addClickListener(e ->{
			 filterText.clear();
			 updateList();
		 });
		 
		CssLayout filtering=new CssLayout();
		 filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		filtering.addComponents(filterText,clearFilterTextBtn);
		
		
		Button addCustomer=new Button("Add Customer");
		addCustomer.addClickListener(e ->{
			grid.select(null);
			form.setCustomer(new Customer());
		});
		
		HorizontalLayout toolbar=new HorizontalLayout(filtering, addCustomer);
		toolbar.setSpacing(true);
		
		HorizontalLayout main=new HorizontalLayout(grid,form);
		main.setSpacing(true);
		main.setSizeFull();
		grid.setSizeFull();
		main.setExpandRatio(grid, 1);
		 layout.addComponents(toolbar,main);
		 
		updateList();
		layout.setSpacing(true);
		layout.setMargin(true);
		setContent(layout);
		
		form.setVisible(false);
		grid.addSelectionListener(event ->{
			if(event.getSelected().isEmpty())
			{
				form.setVisible(false);
			}
			else
			{
				Customer customer=(Customer) event.getSelected().iterator().next();
				form.setCustomer(customer);
			}
		});
	}



	public void updateList() {
		List<Customer> customers=service.findAll();
				
				grid.setContainerDataSource(new BeanItemContainer<>(Customer.class,customers));
	}
	
	
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = VaadinprojectUI.class)
	public static class Servlet extends VaadinServlet {
	}

}