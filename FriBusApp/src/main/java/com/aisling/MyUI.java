package com.aisling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Connection connection = null;
        String connectionString = "jdbc:sqlserver://fridayserver.database.windows.net:1433;" + "database=busdbcom673;"
                + "user=dylanob@fridayserver;" + "password=Friday123;" + "encrypt=true;"
                + "trustServerCertificate=false;" + "hostNameInCertificate=*.database.windows.net;"
                + "loginTimeout=30;";
        //to do a connection on the app

        final VerticalLayout layout = new VerticalLayout();
        
        Label logo = new Label("<H1>Fun Bus Bookings</H1> <p/> <h3>Please enter the details below and click Book</h3>", ContentMode.HTML);
        layout.addComponent(logo);


        try {
        // Connect with JDBC driver to a database
        connection = DriverManager.getConnection(connectionString);
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM FriBusTable"); //Is name of table that was created in step 4
        List<Bus> buses = new ArrayList<Bus>();//create a list of bus objects.  call the list buses
        
        while(rs.next()){ 
	    // Add a new bus instantiated with the fields from the record (that we want, we might not want all the fields, note how I skip the id)
	            buses.add(new Bus(
                rs.getInt("id"),//how did he skip id 
                rs.getString("destination"), 
				rs.getString("feature"), 
				rs.getBoolean("accessible"), 
				rs.getInt("capacity")));
        }//while

        //Builds and structure of grid
        Grid<Bus> myGrid = new Grid<> ();
        myGrid.setItems(buses);
        myGrid.addColumn(Bus::getDestination).setCaption("Destination");
        myGrid.addColumn(Bus::getCapacity).setCaption("Capacity");
        myGrid.addColumn(Bus::getFeature).setCaption("Feature");
        myGrid.addColumn(Bus::getIsAccessible).setCaption("Accessible");
        myGrid.setSelectionMode(SelectionMode.MULTI);
        myGrid.setSizeFull();
        
        final HorizontalLayout hLayout = new HorizontalLayout();

        TextField groupName = new TextField("Group Name");
    
        //Slider
        Slider s = new Slider("People", 20, 150);
        s.setValue(20.0);
        s.setWidth("500px");
        s.setCaption("How many people are invited to this party");

        s.addValueChangeListener(e -> {
            int groupTotal = e.getValue().intValue();
        });

        //ComboBox
        ComboBox<String> accesibilityRequired = new ComboBox<String>("Accessibility");
        accesibilityRequired.setItems("yes", "no"); 
    
        hLayout.addComponents(groupName,s, accesibilityRequired);

        Label infoLabel = new Label("Your group is not yet booked", ContentMode.HTML);

        //Book Button
        Button button = new Button("Book");
        button.addClickListener(e -> {
            
        Set<Bus> selected = myGrid.getSelectedItems();

        int totalCapacity = 0;
        int numberInGroup = s.getValue().intValue();

        for(Bus o : selected){
            totalCapacity = totalCapacity + o.getCapacity();
           }//for capacity issues 
            if (numberInGroup > totalCapacity){
                infoLabel.setValue("<strong>You have selected buses with a max capacity of <strong>" + totalCapacity + " <strong>which is not enough to hold </strong>" + numberInGroup);
                return;
            }//if
            
            if (accesibilityRequired.getValue() == "yes"){
                for(Bus o : selected){
                    if (!o.getIsAccessible()){
                    infoLabel.setValue("<strong>You cannot select a non-accessible bus. </strong>");
                    return;
                    }//if alcohol
                }//for
            }  //if accessibility required  

        if(myGrid.getSelectedItems().size() == 0){
            infoLabel.setValue("<strong>Please select at least one bus!</strong>");
            return;
        }//if no bus selected

        if (groupName.getValue().length() == 0) { 
            infoLabel.setValue("<strong>Please enter group name</strong>");
            return;
        }//if no group name

        if(!accesibilityRequired.getSelectedItem().isPresent()){
            infoLabel.setValue("<strong>Please confirm if you need an accessible bus</strong>");
            return;
        }//if accessibility not specified

       
        else{
            infoLabel.setValue("<h3>Success! The group is  booked now<h3>");
                return;
            }//else
        });//button
        


        

        layout.addComponents(hLayout, button, infoLabel ,myGrid);

        }//try

        catch(Exception e){
            // Not success!
            System.out.println(e.getMessage());
        }//catch

       Label studentNumber = new Label("Student number xxxxxx");
        
        layout.addComponent(studentNumber);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
