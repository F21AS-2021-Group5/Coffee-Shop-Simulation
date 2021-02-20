/**
 * Customer.java - class to implement a customer for the coffee shop simulation
 * 
 * @author Esther Rayssiguie 
 * @author Jake Marrocco
 * @author Karolina Judzentyte
 * @author Valerio Franchi
 * @version 0.1
 * 
 * Copyright (c) 2021 
 * All rights reserved.
 */

package CaffeeShopProject;

import java.util.HashMap;
import java.util.Queue;
import java.util.ArrayList;
import java.time.LocalDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class GUI {
	
	// For Shell display
	protected Shell shell;
	private Text txtMenu;
	private Text txt_description;
	private Text txt_tax;
	private Text txt_subt;
	private Text txt_discnt;
	private Text txt_total;
	private Text txtCustomerId;
	private List list_menu;
	private ScrolledComposite scrolledComposite_Menu;
	private List list_order;
	private ScrolledComposite scrolledComposite_Order;
	private List list_Receipt;
	private ScrolledComposite scrolledComposite_Receipt;
	private List list_Report;
	private ScrolledComposite scrolledComposite_Report;
	private Button btnAdd;
	private Button btnRemove;
	
	private int itemQuantity;
	ArrayList<String> customer_order = new ArrayList<String>();	//Order of current customer
	private String[] itemSelected;
	private String description;
	
	
	Customer customer;
	MenuItem menu_item;
	CoffeeShop coffee;
	Cashier cashier;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GUI window = new GUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Open the window
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	// Create contents of the window
	protected void createContents() {
		// Start Shell
		shell = new Shell();
		shell.setSize(800, 700);
		shell.setText("SWT Application");
		
		// Title
		txtMenu = new Text(shell, SWT.BORDER);
		txtMenu.setText("Menu");
		txtMenu.setBounds(10, 10, 51, 26);
		
		DisplayCustomer();
		SelectCategory();
		//DisplayMenu();
		DisplayItemDescription();
		AddItem();
		RemoveItem();
		Purchase();
		DisplayOrder();	
		DisplayReceipt();
		DisplayPrice();
		DisplayFinalReport();

	}
	
	// Create New Customer AND Display Current Customer
	private void DisplayCustomer() {
		Button btnNewCustomer = new Button(shell, SWT.NONE);	// Create New Customer button
		btnNewCustomer.setBounds(198, 10, 109, 26);		// Button size
		btnNewCustomer.setText("New Customer");		// Button text
		// Action New Customer Button
		btnNewCustomer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.redraw();		// Re-initialise shell
				customer_order.removeAll(customer_order);	// Re-initialise current order
				btnAdd.setEnabled(true);	// Add button enable again
				btnRemove.setEnabled(true);	// Remove button again
				list_menu.removeAll();	// empty list
				list_order.removeAll();	// empty list
			}
		});
		
		// Display Current Customer and their ID
		txtCustomerId = new Text(shell, SWT.BORDER);
		txtCustomerId.setText("Customer ID " + customer.getId());
		txtCustomerId.setBounds(10, 55, 298, 26);
	}
	
	// Select category and fill list of items from category chosen
	private void SelectCategory() {
		scrolledComposite_Menu = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_Menu.setBounds(10, 122, 298, 206);
		scrolledComposite_Menu.setExpandHorizontal(true);
		scrolledComposite_Menu.setExpandVertical(true);
		
		list_menu = new List(scrolledComposite_Menu, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		// When you select from list
		list_menu.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				itemSelected = list_menu.getSelection();	// Add selected item to itemSelected list
			}
			public void widgetDefaultSelected(SelectionEvent event) {
		        itemSelected = list_menu.getSelection();	// Add selected item to itemSelected list
			}
//			public void widgetDefaultSelected(SelectionEvent event) {
//		        int[] selections = list_menu.getSelectionIndices();
//		        String outText = "";
//		        for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
//		          outText += selections[loopIndex] + " ";
//		        System.out.println("You selected: " + outText);
//		      }
		});
		scrolledComposite_Menu.setContent(list_menu);
		scrolledComposite_Menu.setMinSize(list_menu.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		
		Button btnMeal = new Button(shell, SWT.NONE);
		btnMeal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				list_menu.removeAll();  // empty list
				int iCount = coffee.menu.size(); // go through size of the menu
				for (int i = 0; i < iCount; i++) {
					if (menu_item.getCategory() == "Food") {
						list_menu.add(menu_item.getName()); // add items to list
					}
				}
			}
		});
		btnMeal.setText("Meal");
		btnMeal.setBounds(10, 90, 66, 26);
		
		
		Button btnDessert = new Button(shell, SWT.NONE);
		btnDessert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				list_menu.removeAll();	// empty list
				int iCount = coffee.menu.size(); // go through size of the menu
				for (int i = 0; i < iCount; i++) {
					if (menu_item.getCategory() == "Pastry") {
						list_menu.add(menu_item.getName()); // add item to list
					}
				}
			}
		});
		btnDessert.setText("Desserts");
		btnDessert.setBounds(168, 90, 66, 26);
		
		Button btnDrink = new Button(shell, SWT.NONE);
		btnDrink.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				list_menu.removeAll();	// empty list
				int iCount = coffee.menu.size(); // go through size of the menu
				for (int i = 0; i < iCount; i++) {
					if (menu_item.getCategory() == "Drink") {
						list_menu.add(menu_item.getName()); // add item to list
					}
				}
			}
		});
		btnDrink.setText("Drinks");
		btnDrink.setBounds(242, 90, 66, 26);
		
		Button btnSide = new Button(shell, SWT.NONE);
		btnSide.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				list_menu.removeAll();	// empty list
				int iCount = coffee.menu.size(); // go through size of the menu
				for (int i = 0; i < iCount; i++) {
					if (menu_item.getCategory() == "Side") {
						list_menu.add(menu_item.getName()); // add item to list
					}
				}			
			}
		});
		btnSide.setText("Sides");
		btnSide.setBounds(82, 90, 66, 26);
		
	}
	
//	// Display Menu
//	private void DisplayMenu() {
//
//	}
	
	private void AddItem() {
		// Quantity of the Item selected
		Spinner spinner = new Spinner(shell, SWT.BORDER);
		spinner.setBounds(91, 336, 59, 26);
		itemQuantity = spinner.getIncrement();	// Get quantity of item selected
		
		// Add Item and its quantity desired
		btnAdd = new Button(shell, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				for(int i = 0; i< coffee.menu.size(); i ++) {
					if(itemSelected.toString() == menu_item.getName()) {	// When item selected found, add Item
						if(itemQuantity > 0) {	// if itemQuantity larger than 0
							for (int j = 0; i < itemQuantity; i++)
								customer_order.add(menu_item.getName());	//add item to order_list number of time it was selected
							
							LocalDateTime time = customer.timestamp;
							try {
								customer.addItem(menu_item.getIdentifier(), itemQuantity, time);	//add item & its quantity
							} catch (InvalidMenuItemQuantityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvalidMenuItemDataException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}	
					}
				}				
			}
		});
		btnAdd.setBounds(168, 334, 51, 30);
		btnAdd.setText("Add");
	}
	
	// Remove Selected item from the list
	private void RemoveItem() {
		btnRemove = new Button(shell, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				for(int i = 0; i< coffee.menu.size(); i ++) {
					if(itemSelected.toString() == menu_item.getName()) {	// When item selected found, remove Item
						try {
							customer.removeItem(menu_item.getIdentifier(), itemQuantity);
						} catch (NoMatchingMenuItemIDException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidMenuItemQuantityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidMenuItemDataException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidCartItemException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}	
			}
		});
		btnRemove.setBounds(10, 334, 66, 30);
		btnRemove.setText("Remove");
	}
	
	private void Purchase() {
		Button btnPurchase = new Button(shell, SWT.NONE);
		btnPurchase.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnAdd.setEnabled(false);	// Disable Add button
				btnRemove.setEnabled(false);	// Disable Remove button
			}
		});
		btnPurchase.setBounds(237, 334, 70, 30);
		btnPurchase.setText("Purchase");
	}
	
	// Display description of item chosen
	private void DisplayItemDescription() {
		txt_description = new Text(shell, SWT.BORDER);
		
		//get the description of the item selected
		for(int i = 0; i< coffee.menu.size(); i ++) {
			if(itemSelected.toString() == menu_item.getName()) {
				description = menu_item.getDescription();
			}
		}
		txt_description.setText("Item Description " + description);
		txt_description.setBounds(10, 378, 297, 60);
	}
	
	private void DisplayPrice() {
		txt_tax = new Text(shell, SWT.BORDER);
		txt_tax.setText("Tax  " + cashier.getCartTax());
		txt_tax.setBounds(10, 458, 190, 30);
		
		txt_subt = new Text(shell, SWT.BORDER);
		txt_subt.setText("Subtotal  " + cashier.getCartSubtotalPrice());
		txt_subt.setBounds(10, 513, 190, 30);
		
		txt_discnt = new Text(shell, SWT.BORDER);
		txt_discnt.setText("Discount  " + cashier.getDiscount());
		txt_discnt.setBounds(10, 566, 190, 30);
		
		txt_total = new Text(shell, SWT.BORDER);
		txt_total.setText("Total  " + customer.getCartTotalPrice());
		txt_total.setBounds(10, 613, 190, 30);
	}
	
	// Display List of Current Order from Current Customer
	private void DisplayOrder() {
		Button btnOrderList = new Button(shell, SWT.NONE);
		btnOrderList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				list_order.removeAll();	// empty list
				for(int i = 0; i<customer_order.size(); i++) {
					list_order.add(customer_order.toString());	// Show Order list
				}			
			}
		});
		btnOrderList.setBounds(217, 456, 90, 30);
		btnOrderList.setText("Order List");
		
		scrolledComposite_Order = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_Order.setBounds(360, 10, 177, 245);
		scrolledComposite_Order.setExpandHorizontal(true);
		scrolledComposite_Order.setExpandVertical(true);
		
		list_order = new List(scrolledComposite_Order, SWT.BORDER);
		scrolledComposite_Order.setContent(list_order);
		scrolledComposite_Order.setMinSize(list_order.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	// Display Receipt of Current Customer
	private void DisplayReceipt() {
		Button btnReceipt = new Button(shell, SWT.NONE);
		btnReceipt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				list_Receipt.add(customer.receipt());	// Display receipt		
			}
		});
		btnReceipt.setBounds(217, 511, 90, 30);
		btnReceipt.setText("Receipt");
		
		scrolledComposite_Receipt = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_Receipt.setExpandVertical(true);
		scrolledComposite_Receipt.setExpandHorizontal(true);
		scrolledComposite_Receipt.setBounds(360, 277, 177, 366);
		
		list_Receipt = new List(scrolledComposite_Receipt, SWT.BORDER);
		scrolledComposite_Receipt.setContent(list_Receipt);
		scrolledComposite_Receipt.setMinSize(list_Receipt.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	// Display Final Report of Coffee Shop
	private void DisplayFinalReport() {
		Button btnFinalReportCoffee = new Button(shell, SWT.NONE);
		btnFinalReportCoffee.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//list_Report.add(coffee.); SHOW FINAL REPORT
			}
		});
		btnFinalReportCoffee.setBounds(217, 613, 90, 30);
		btnFinalReportCoffee.setText("Final Report");
		
		scrolledComposite_Report = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_Report.setExpandVertical(true);
		scrolledComposite_Report.setExpandHorizontal(true);
		scrolledComposite_Report.setBounds(559, 10, 213, 633);
		
		list_Report = new List(scrolledComposite_Report, SWT.BORDER);
		scrolledComposite_Report.setContent(list_Report);
		scrolledComposite_Report.setMinSize(list_Report.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void update() {
		SelectCategory();
		DisplayOrder();	
		DisplayReceipt();
		DisplayFinalReport();
		
		shell.redraw();
    }
	
    public void run() {
        while (true) {
            update();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
	
}

