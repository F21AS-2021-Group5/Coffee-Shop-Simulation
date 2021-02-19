package CaffeeShopProject;

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

public class GUI {

	protected Shell shell;
	private Text txtMenu;
	private Text txt_description;
	private Text txt_tax;
	private Text txt_subt;
	private Text txt_discnt;
	private Text txt_total;
	private Text txtCustomerId;
	
	Customer customer;
	MenuItem menu_item;

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

	/**
	 * Open the window.
	 */
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

	/**
	 * Create contents of the window.
	 */
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
		DisplayMenu();
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
				shell.close();		// CHANGE LATER, for now just close window
			}
		});
		
		// Display Current Customer and their ID
		txtCustomerId = new Text(shell, SWT.BORDER);
		txtCustomerId.setText("Customer ID" ); //+ customer.getId()
		txtCustomerId.setBounds(10, 55, 298, 26);
	}
	
	private void SelectCategory() {
		Button btnMeal = new Button(shell, SWT.NONE);
		btnMeal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnMeal.setText("Meal");
		btnMeal.setBounds(10, 90, 66, 26);
		
		
		Button btnDessert = new Button(shell, SWT.NONE);
		btnDessert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnDessert.setText("Desserts");
		btnDessert.setBounds(168, 90, 66, 26);
		
		Button btnDrink = new Button(shell, SWT.NONE);
		btnDrink.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnDrink.setText("Drinks");
		btnDrink.setBounds(242, 90, 66, 26);
		
		Button btnSide = new Button(shell, SWT.NONE);
		btnSide.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
			}
		});
		btnSide.setText("Sides");
		btnSide.setBounds(82, 90, 66, 26);
		
	}
	
	// Display Menu
	private void DisplayMenu() {
		ScrolledComposite scrolledComposite_Menu = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_Menu.setBounds(10, 122, 298, 206);
		scrolledComposite_Menu.setExpandHorizontal(true);
		scrolledComposite_Menu.setExpandVertical(true);
		
		List list_menu = new List(scrolledComposite_Menu, SWT.BORDER);
		scrolledComposite_Menu.setContent(list_menu);
		scrolledComposite_Menu.setMinSize(list_menu.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	private void AddItem() {
		// Quantity of the Item selected
		Spinner spinner = new Spinner(shell, SWT.BORDER);
		spinner.setBounds(91, 336, 59, 26);
		
		// Add Item and its quantity desired
		Button btnAdd = new Button(shell, SWT.NONE);
		btnAdd.setBounds(168, 334, 51, 30);
		btnAdd.setText("Add");
	}
	
	private void RemoveItem() {
		Button btnRemove = new Button(shell, SWT.NONE);
		btnRemove.setBounds(10, 334, 66, 30);
		btnRemove.setText("Remove");
	}
	
	private void Purchase() {
		Button btnPurchase = new Button(shell, SWT.NONE);
		btnPurchase.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnPurchase.setBounds(237, 334, 70, 30);
		btnPurchase.setText("Purchase");
	}
	
	private void DisplayItemDescription() {
		txt_description = new Text(shell, SWT.BORDER);
		txt_description.setText("Item Description" ); //+ menu_item.getDescription()
		txt_description.setBounds(10, 378, 297, 60);
	}
	
	private void DisplayPrice() {
		txt_tax = new Text(shell, SWT.BORDER);
		txt_tax.setText("Tax");
		txt_tax.setBounds(10, 458, 190, 30);
		
		txt_subt = new Text(shell, SWT.BORDER);
		txt_subt.setText("Subtotal");
		txt_subt.setBounds(10, 513, 190, 30);
		
		txt_discnt = new Text(shell, SWT.BORDER);
		txt_discnt.setText("Discount");
		txt_discnt.setBounds(10, 566, 190, 30);
		
		txt_total = new Text(shell, SWT.BORDER);
		txt_total.setText("Total");
		txt_total.setBounds(10, 613, 190, 30);
	}
	
	// Display List of Current Order from Current Customer
	private void DisplayOrder() {
		Button btnOrderList = new Button(shell, SWT.NONE);
		btnOrderList.setBounds(217, 456, 90, 30);
		btnOrderList.setText("Order List");
		
		ScrolledComposite scrolledComposite_Order = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_Order.setBounds(360, 10, 177, 245);
		scrolledComposite_Order.setExpandHorizontal(true);
		scrolledComposite_Order.setExpandVertical(true);
		
		List list_order = new List(scrolledComposite_Order, SWT.BORDER);
		scrolledComposite_Order.setContent(list_order);
		scrolledComposite_Order.setMinSize(list_order.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	// Display Receipt of Current Customer
	private void DisplayReceipt() {
		Button btnReceipt = new Button(shell, SWT.NONE);
		btnReceipt.setBounds(217, 511, 90, 30);
		btnReceipt.setText("Receipt");
		
		ScrolledComposite scrolledComposite_Receipt = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_Receipt.setExpandVertical(true);
		scrolledComposite_Receipt.setExpandHorizontal(true);
		scrolledComposite_Receipt.setBounds(360, 277, 177, 366);
		
		List list_Receipt = new List(scrolledComposite_Receipt, SWT.BORDER);
		scrolledComposite_Receipt.setContent(list_Receipt);
		scrolledComposite_Receipt.setMinSize(list_Receipt.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	// Display Final Report of Coffee Shop
	private void DisplayFinalReport() {
		Button btnFinalReportCoffee = new Button(shell, SWT.NONE);
		btnFinalReportCoffee.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnFinalReportCoffee.setBounds(217, 613, 90, 30);
		btnFinalReportCoffee.setText("Final Report");
		
		ScrolledComposite scrolledComposite_Report = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_Report.setExpandVertical(true);
		scrolledComposite_Report.setExpandHorizontal(true);
		scrolledComposite_Report.setBounds(559, 10, 213, 633);
		
		List list_Report = new List(scrolledComposite_Report, SWT.BORDER);
		scrolledComposite_Report.setContent(list_Report);
		scrolledComposite_Report.setMinSize(list_Report.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void update() {
		SelectCategory();
		DisplayMenu();
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

