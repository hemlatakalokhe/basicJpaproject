package com.example.vaadinproject;



import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class CustomerForm extends FormLayout
{
	private TextField firstName=new TextField("First Name");
	private TextField lastName=new TextField("Last Name");
	private TextField email=new TextField("Email");
	private NativeSelect status=new NativeSelect("Status");
	private DateField birthDate=new DateField("BirthDate");
	private Button save=new Button("Save");
	private Button delete=new Button("Delete");
	
	private CustomerService customerService=CustomerService.getInstance();
	private Customer customer;
	private VaadinprojectUI vaadinUI;
	
	public CustomerForm(VaadinprojectUI vaadinUI) {
		this.vaadinUI=vaadinUI;
		status.addItems(CustomerStatus.values());
		status.setStyleName(ValoTheme.BUTTON_PRIMARY);
		
		save.setClickShortcut(KeyCode.ENTER);
		
		save.addClickListener(e->save());
		delete.addClickListener(e->delete());
		
		setSizeUndefined();
		HorizontalLayout buttons=new HorizontalLayout(save,delete);
		buttons.setSpacing(true);
		addComponents(firstName,lastName,email,status,birthDate,buttons);
		}
	
	public void setCustomer(Customer customer)
	{
		this.customer=customer;
		
		BeanFieldGroup.bindFieldsUnbuffered(customer, this);
		
		delete.setVisible(customer.isPersisted());
		setVisible(true);
		firstName.selectAll();
	}
	
	public void save()
	{
		customerService.save(customer);
		vaadinUI.updateList();
		setVisible(false);
	}
	public void delete()
	{
		customerService.delete(customer);
		vaadinUI.updateList();
		setVisible(false);
	}
}
