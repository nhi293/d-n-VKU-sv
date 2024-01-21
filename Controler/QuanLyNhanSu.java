package Controler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class QuanLyNhanSu extends JFrame {
    private JTable dataTable;
    private JTable dataTable1;
    private JTable dataTable2;
    private DefaultTableModel dataTableModel;
    private  String url = "jdbc:mysql://localhost:3306/nhansu";
    private  String user = "root";
    private  String password = "123456789";
    

    public QuanLyNhanSu() {
        createAndShowGUI();
    }

    public void createAndShowGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Công Ty TNHH ABC Đà Nẵng");
        setSize(600, 400);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panel1 = createEmployeeTab();
        JPanel panel2 = Createotherdepartments("Content for Tab 2");
        JPanel panel3 = Createotherprojects("Content for Tab 3");

        tabbedPane.addTab("Nhân viên", panel1);
        tabbedPane.addTab("Phòng ban", panel2);
        tabbedPane.addTab("Dự án", panel3);
        

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private JPanel createEmployeeTab() {
        JPanel panel = new JPanel(new BorderLayout());
        

        dataTableModel = new DefaultTableModel();
        dataTableModel.addColumn("Mã nhân viên");
        dataTableModel.addColumn("Địa chỉ");
        dataTableModel.addColumn("Họ tên");
        dataTableModel.addColumn("Lương");
        dataTableModel.addColumn("Ngày sinh");
        dataTableModel.addColumn("Phòng");
        dataTableModel.addColumn("Dự án");
        

        dataTable = new JTable(dataTableModel);
        dataTable.getTableHeader().setBackground(new Color(70, 130, 180));
        JScrollPane scrollPane = new JScrollPane(dataTable);
       

        // Adding the table and scroll bars to the panel
        panel.add(scrollPane, BorderLayout.CENTER);

        // Adding search components
        JPanel searchPanel = new JPanel(new GridLayout(1, 0));
        JLabel search = new JLabel("Tìm kiếm");
        JTextField searchs = new JTextField();
        JButton searchss = new JButton("Tìm kiếm");
        searchPanel.add(search);
        searchPanel.add(searchs);
        searchPanel.add(searchss);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Adding buttons and input fields
        JPanel buttonAndInputPanel = new JPanel(new BorderLayout());

        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2));
        inputPanel.add(new JLabel("Mã nhân viên"));
        inputPanel.add(new JTextField());
        inputPanel.add(new JLabel("Địa chỉ"));
        inputPanel.add(new JTextField());
        inputPanel.add(new JLabel("Họ tên"));
        inputPanel.add(new JTextField());
        inputPanel.add(new JLabel("Lương"));
        inputPanel.add(new JTextField());
        inputPanel.add(new JLabel("Ngày sinh"));
        inputPanel.add(new JTextField());
        inputPanel.add(new JLabel("Phòng"));
        inputPanel.add(new JTextField());
        inputPanel.add(new JLabel("Dự án"));
        inputPanel.add(new JTextField());
        inputPanel.setBackground(new Color(0, 130, 180));
        buttonAndInputPanel.add(inputPanel, BorderLayout.NORTH);	
        
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
 

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

     
        buttonAndInputPanel.add(buttonPanel, BorderLayout.WEST);


        panel.add(buttonAndInputPanel, BorderLayout.SOUTH);
        

        // Thêm lắng nghe cho nút "Thêm"
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String manhanvien = ((JTextField) inputPanel.getComponent(1)).getText();
                String diachi = ((JTextField) inputPanel.getComponent(3)).getText();
                String hoten = ((JTextField) inputPanel.getComponent(5)).getText();
                String luong = ((JTextField) inputPanel.getComponent(7)).getText();
                String ngaysinh = ((JTextField) inputPanel.getComponent(9)).getText();
                String phong = ((JTextField) inputPanel.getComponent(11)).getText();
                String duan = ((JTextField) inputPanel.getComponent(13)).getText();

                // Kết nối đến MySQL
                try (Connection connection = DriverManager.getConnection(url,user,password)) {
                    // Sử dụng PreparedStatement để tránh SQL Injection
                    String sql = "INSERT INTO `nhân viên` (`Mã nhân viên`, `Địa chỉ`, `Họ tên`,`Lương`, `Ngày sinh`, `Phòng`,`Dự án`) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, manhanvien);
                        preparedStatement.setString(2, diachi);
                        preparedStatement.setString(3, hoten);
                        preparedStatement.setString(4, luong);
                        preparedStatement.setString(5, ngaysinh);
                        preparedStatement.setString(6, phong);
                        preparedStatement.setString(7, duan);


                        // Thực hiện truy vấn INSERT
                        preparedStatement.executeUpdate();
                        System.out.println("The data has been added to the database");
                    }

                    // Thêm dữ liệu vào bảng
                    dataTableModel.addRow(new Object[]{manhanvien, diachi, hoten,luong, ngaysinh, phong, duan});

                    // Xóa nội dung của các trường dữ liệu sau khi thêm
                    ((JTextField) inputPanel.getComponent(1)).setText("");
                    ((JTextField) inputPanel.getComponent(3)).setText("");
                    ((JTextField) inputPanel.getComponent(5)).setText("");
                    ((JTextField) inputPanel.getComponent(7)).setText("");
                    ((JTextField) inputPanel.getComponent(9)).setText("");
                    ((JTextField) inputPanel.getComponent(11)).setText("");
                    ((JTextField) inputPanel.getComponent(13)).setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    
                }
            }
        });
        
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = dataTable.getSelectedRow();

                if (selectedRow != -1) {
                    String manhanvien = dataTableModel.getValueAt(selectedRow, 0).toString();
                    String diachi = dataTableModel.getValueAt(selectedRow, 1).toString();
                    String tennhanvien = dataTableModel.getValueAt(selectedRow, 2).toString();
                    String luong = dataTableModel.getValueAt(selectedRow, 3).toString();
                    String ngaysinh = dataTableModel.getValueAt(selectedRow, 4).toString();
                    String phong = dataTableModel.getValueAt(selectedRow, 5).toString();
                    String duan = dataTableModel.getValueAt(selectedRow, 6).toString();

                    // Tạo một bản sao của inputPanel để hiển thị trong dialog
                    JPanel clonedInputPanel = cloneInputPanel1(inputPanel);

                    // Hiển thị trên giao diện chính để sửa thông tin
                    ((JTextField) clonedInputPanel.getComponent(1)).setText(manhanvien);
                    ((JTextField) clonedInputPanel.getComponent(3)).setText(diachi);
                    ((JTextField) clonedInputPanel.getComponent(5)).setText(tennhanvien);
                    ((JTextField) clonedInputPanel.getComponent(7)).setText(luong);
                    ((JTextField) clonedInputPanel.getComponent(9)).setText(ngaysinh);
                    ((JTextField) clonedInputPanel.getComponent(11)).setText(phong);
                    ((JTextField) clonedInputPanel.getComponent(13)).setText(duan);

                    // Hiển thị dialog
                    int result = JOptionPane.showConfirmDialog(null, clonedInputPanel, "Sửa Thông Tin", JOptionPane.OK_CANCEL_OPTION);

                    if (result == JOptionPane.OK_OPTION) {
                        // Lấy thông tin mới từ giao diện chính
                    	manhanvien = ((JTextField) clonedInputPanel.getComponent(1)).getText();
                    	diachi = ((JTextField) clonedInputPanel.getComponent(3)).getText();
                    	tennhanvien = ((JTextField) clonedInputPanel.getComponent(5)).getText();
                    	luong = ((JTextField) clonedInputPanel.getComponent(7)).getText();
                    	ngaysinh = ((JTextField) clonedInputPanel.getComponent(9)).getText();
                    	phong = ((JTextField) clonedInputPanel.getComponent(11)).getText();
                    	duan = ((JTextField) clonedInputPanel.getComponent(13)).getText();

                        // Cập nhật dữ liệu trong bảng
                        dataTableModel.setValueAt(manhanvien, selectedRow, 0);
                        dataTableModel.setValueAt(diachi, selectedRow, 1);
                        dataTableModel.setValueAt(tennhanvien, selectedRow, 2);
                        dataTableModel.setValueAt(luong, selectedRow, 3);
                        dataTableModel.setValueAt(ngaysinh, selectedRow, 4);
                        dataTableModel.setValueAt(phong, selectedRow, 5);
                        dataTableModel.setValueAt(duan, selectedRow, 6);

                        // Cập nhật dữ liệu trong cơ sở dữ liệu
                        updateDatabase1(manhanvien, diachi, tennhanvien, luong, ngaysinh, phong, duan);

                        // Thông báo rằng thông tin đã được cập nhật
                        JOptionPane.showMessageDialog(null, "Thông tin đã được cập nhật.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    // Thông báo rằng không có hàng nào được chọn
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng để sửa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            private JPanel cloneInputPanel1(JPanel inputPanel) {
                JPanel clone = new JPanel(new GridLayout(8, 2));

                for (Component component : inputPanel.getComponents()) {
                    if (component instanceof JLabel) {
                        JLabel label = new JLabel(((JLabel) component).getText());
                        clone.add(label);
                    } else if (component instanceof JTextField) {
                        JTextField textField = new JTextField();
                        clone.add(textField);
                    }
                }

                return clone;
            }

            private void updateDatabase1(String manhanvien, String diachi, String tennhanvien, String luong, String ngaysinh, String phong, String duan) {
            	try (Connection connection = DriverManager.getConnection(url, user, password)) {
            		String sql = "UPDATE `nhansu`.`nhân viên` SET  `Địa chỉ` = ?, `Họ tên` = ?, `Lương` = ?, `Ngày sinh` = ?, `Phòng` = ?, `Dự án` = ? WHERE `Mã nhân viên` = ?;";
            	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            	    	
            	        preparedStatement.setString(1, diachi);
            	        preparedStatement.setString(2, tennhanvien);
            	        preparedStatement.setString(3, luong);
            	        preparedStatement.setString(4, ngaysinh);
            	        preparedStatement.setString(5, phong);
            	        preparedStatement.setString(6, duan);
            	        preparedStatement.setString(7, manhanvien);
            	        
            	        // Thực hiện truy vấn UPDATE
            	        preparedStatement.executeUpdate();

            	        System.out.println("Data updated successfully in the database.");
            	    }
            	} catch (SQLException ex) {
            	    ex.printStackTrace();
            	}

            }


        });


        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = dataTable.getSelectedRows();

                if (selectedRows.length > 0) {
                    // Hỏi người dùng xác nhận việc xóa
                    int option = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa các hàng đã chọn?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        // Xóa từng hàng được chọn
                        for (int i = selectedRows.length - 1; i >= 0; i--) {
                            String manhanvien = dataTableModel.getValueAt(selectedRows[i], 0).toString();
                            deleteFromDatabase(manhanvien); 
                            dataTableModel.removeRow(selectedRows[i]);
                        }
                    }
                } else {
                    // Thông báo rằng không có hàng nào được chọn
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn ít nhất một hàng để xóa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            private void deleteFromDatabase(String manhanvien) {
           	    try (Connection connection = DriverManager.getConnection(url,user,password)) {
           	        String sql = "DELETE FROM `nhansu`.`nhân viên` WHERE `Mã nhân viên` = ?"; 
           	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
           	            preparedStatement.setString(1, manhanvien);
           	            // Thực hiện truy vấn DELETE
           	            preparedStatement.executeUpdate();
           	            System.out.println("The data has been deleted from the database");
           	        }
           	    } catch (SQLException ex) {
           	        ex.printStackTrace();
           	        	
           	    }
           	}

        });
        
        
        
        searchss.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchs.getText().trim().toLowerCase();

                if (!keyword.isEmpty()) {
                    // Tìm kiếm và hiển thị kết quả
                    filterTable(dataTableModel, keyword);
                } else {
                    // Nếu từ khóa trống, hiển thị toàn bộ dữ liệu
                    resetTable(dataTableModel);
                }
            }

            private void filterTable(DefaultTableModel table, String keyword) {
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(table);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), 0, 1, 2, 3, 4, 5, 6));
                dataTable.setRowSorter(sorter);
            }

            private void resetTable(DefaultTableModel table) {
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(table);
                sorter.setRowFilter(null);
                dataTable.setRowSorter(sorter);
            }
        });

        
        
        ketnoinhanvienMysql();

        return panel;
    }

    private void ketnoinhanvienMysql() {
        try {
           
            try (Connection connection = DriverManager.getConnection(url, user, password);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM nhansu.`nhân viên`")) {

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                DefaultTableModel tableModel = (DefaultTableModel) dataTable.getModel();
                tableModel.setRowCount(0); // Clear existing rows

                while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = resultSet.getObject(i);
                    }
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ hoặc thông báo lỗi theo yêu cầu của bạn
        }
    }

    
    
    
    

    private JPanel Createotherdepartments(String content) {
    	JPanel panel1 = new JPanel(new BorderLayout());
    	 DefaultTableModel dataTableModel1 = new DefaultTableModel();
    	 dataTableModel1.addColumn("Mã Phòng Ban");
    	 dataTableModel1.addColumn("Tên phòng ban");
    	 dataTableModel1.addColumn("Trưởng Phòng");
    	 dataTableModel1.addColumn("Số điện thoại Phòng");
    	 
    	 dataTable1 = new JTable(dataTableModel1);
         dataTable1.getTableHeader().setBackground(new Color(70, 130, 180));
         JScrollPane scrollPane = new JScrollPane(dataTable1);
        

         // Adding the table and scroll bars to the panel
         panel1.add(scrollPane, BorderLayout.CENTER);

         // Adding search components
         JPanel searchPanel1 = new JPanel(new GridLayout(1, 0));
         JLabel search1 = new JLabel("Tìm kiếm");
         JTextField searchs1 = new JTextField();
         JButton searchss1 = new JButton("Tìm kiếm");
         searchPanel1.add(search1);
         searchPanel1.add(searchs1);
         searchPanel1.add(searchss1);
         panel1.add(searchPanel1, BorderLayout.NORTH);

         // Adding buttons and input fields
         JPanel buttonAndInputPanel1 = new JPanel(new BorderLayout());
         
         JPanel inputPanel1 = new JPanel();
         inputPanel1.setLayout(new GridLayout(4, 2));
         inputPanel1.add(new JLabel("Mã phòng ban"));
         inputPanel1.add(new JTextField());
         inputPanel1.add(new JLabel("Tên phòng ban"));
         inputPanel1.add(new JTextField());
         inputPanel1.add(new JLabel("Trưởng phòng"));
         inputPanel1.add(new JTextField());
         inputPanel1.add(new JLabel("Số điện thoại phòng"));
         inputPanel1.add(new JTextField());
         
         
         
         inputPanel1.setBackground(new Color(0, 130, 180));
         buttonAndInputPanel1.add(inputPanel1, BorderLayout.NORTH);
         
         
         JPanel buttonPanel1 = new JPanel(new FlowLayout());
         JButton addButton1 = new JButton("Thêm");
         JButton editButton1 = new JButton("Sửa");
         JButton deleteButton1 = new JButton("Xóa");
 

         buttonPanel1.add(addButton1);
         buttonPanel1.add(editButton1);
         buttonPanel1.add(deleteButton1);
   
         buttonAndInputPanel1.add(buttonPanel1, BorderLayout.WEST);


         panel1.add(buttonAndInputPanel1, BorderLayout.SOUTH);

         // Thêm lắng nghe cho nút "Thêm"
         addButton1.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 String maphongban = ((JTextField) inputPanel1.getComponent(1)).getText();
                 String tenphongban = ((JTextField) inputPanel1.getComponent(3)).getText();
                 String truongphong = ((JTextField) inputPanel1.getComponent(5)).getText();
                 String sodienthoai = ((JTextField) inputPanel1.getComponent(7)).getText();

                 // Kết nối đến MySQL
                 try (Connection connection = DriverManager.getConnection(url,user,password)) {
                     // Sử dụng PreparedStatement để tránh SQL Injection
                     String sql = "INSERT INTO `phòng ban` (`Mã phòng ban`, `Tên phòng ban`, `Trưởng phòng`,`Số điện thoại Phòng`) VALUES (?, ?, ?, ?)";
                     try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                         preparedStatement.setString(1, maphongban);
                         preparedStatement.setString(2, tenphongban);
                         preparedStatement.setString(3, truongphong);
                         preparedStatement.setString(4, sodienthoai);


                         // Thực hiện truy vấn INSERT
                         preparedStatement.executeUpdate();
                         System.out.println("The data has been added to the database");
                     }

                     // Thêm dữ liệu vào bảng
                     dataTableModel1.addRow(new Object[]{maphongban, tenphongban, truongphong,sodienthoai});

                     // Xóa nội dung của các trường dữ liệu sau khi thêm
                     ((JTextField) inputPanel1.getComponent(1)).setText("");
                     ((JTextField) inputPanel1.getComponent(3)).setText("");
                     ((JTextField) inputPanel1.getComponent(5)).setText("");
                     ((JTextField) inputPanel1.getComponent(7)).setText("");
                 } catch (SQLException ex) {
                     ex.printStackTrace();
                     // Xử lý lỗi kết nối hoặc thực hiện truy vấn
                 }
             }
         });
         
         
         editButton1.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 int selectedRow1 = dataTable1.getSelectedRow();

                 if (selectedRow1 != -1) {
                     String maphongban = dataTableModel1.getValueAt(selectedRow1, 0).toString();
                     String tenphongban = dataTableModel1.getValueAt(selectedRow1, 1).toString();
                     String truongphong = dataTableModel1.getValueAt(selectedRow1, 2).toString();
                     String sodienthoai = dataTableModel1.getValueAt(selectedRow1, 3).toString();

                     // Tạo một bản sao của inputPanel để hiển thị trong dialog
                     JPanel clonedInputPanel1 = cloneInputPanel1(inputPanel1);

                     // Hiển thị trên giao diện chính để sửa thông tin
                     ((JTextField) clonedInputPanel1.getComponent(1)).setText(maphongban);
                     ((JTextField) clonedInputPanel1.getComponent(3)).setText(tenphongban);
                     ((JTextField) clonedInputPanel1.getComponent(5)).setText(truongphong);
                     ((JTextField) clonedInputPanel1.getComponent(7)).setText(sodienthoai);

                     // Hiển thị dialog
                     int result1 = JOptionPane.showConfirmDialog(null, clonedInputPanel1, "Sửa Thông Tin", JOptionPane.OK_CANCEL_OPTION);

                     if (result1 == JOptionPane.OK_OPTION) {
                         // Lấy thông tin mới từ giao diện chính
                         maphongban = ((JTextField) clonedInputPanel1.getComponent(1)).getText();
                         tenphongban = ((JTextField) clonedInputPanel1.getComponent(3)).getText();
                         truongphong = ((JTextField) clonedInputPanel1.getComponent(5)).getText();
                         sodienthoai = ((JTextField) clonedInputPanel1.getComponent(7)).getText();

                         // Cập nhật dữ liệu trong bảng
                         dataTableModel1.setValueAt(maphongban, selectedRow1, 0);
                         dataTableModel1.setValueAt(tenphongban, selectedRow1, 1);
                         dataTableModel1.setValueAt(truongphong, selectedRow1, 2);
                         dataTableModel1.setValueAt(sodienthoai, selectedRow1, 3);

                         // Cập nhật dữ liệu trong cơ sở dữ liệu
                         updateDatabase1(maphongban, tenphongban, truongphong, sodienthoai);

                         // Thông báo rằng thông tin đã được cập nhật
                         JOptionPane.showMessageDialog(null, "Thông tin đã được cập nhật.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                     }
                 } else {
                     // Thông báo rằng không có hàng nào được chọn
                     JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng để sửa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                 }
             }

             private JPanel cloneInputPanel1(JPanel inputPanel1) {
                 JPanel clone1 = new JPanel(new GridLayout(8, 2));

                 for (Component component1 : inputPanel1.getComponents()) {
                     if (component1 instanceof JLabel) {
                         JLabel label1 = new JLabel(((JLabel) component1).getText());
                         clone1.add(label1);
                     } else if (component1 instanceof JTextField) {
                         JTextField textField1 = new JTextField();
                         clone1.add(textField1);
                     }
                 }

                 return clone1;
             }

             private void updateDatabase1(String maphongban, String tenphongban, String truongphong, String sodienthoai) {
                 try (Connection connection1 = DriverManager.getConnection(url,user,password)) {
                     String sql1 = "UPDATE `nhansu`.`phòng ban` SET `Tên phòng ban` = ?, `Trưởng phòng` = ?, `Số điện thoại Phòng` = ? WHERE `Mã phòng ban` = ?";
                     try (PreparedStatement preparedStatement1 = connection1.prepareStatement(sql1)) {       
                    	 preparedStatement1.setString(1, tenphongban);
                    	 preparedStatement1.setString(2, truongphong);
                    	 preparedStatement1.setString(3, sodienthoai);
                    	 preparedStatement1.setString(4, maphongban);

                         // Thực hiện truy vấn UPDATE
                         preparedStatement1.executeUpdate();
             	        System.out.println("Data updated successfully in the database.");

                     }
                 } catch (SQLException ex) {
                     ex.printStackTrace();
                     
                    
                 }
             }
         });



         
         deleteButton1.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 int[] selectedRows1 = dataTable1.getSelectedRows();

                 if (selectedRows1.length > 0) {
                     // Hỏi người dùng xác nhận việc xóa
                     int option1 = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa các hàng đã chọn?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

                     if (option1 == JOptionPane.YES_OPTION) {
                         // Xóa từng hàng được chọn
                         for (int i = selectedRows1.length - 1; i >= 0; i--) {
                             String maphongban = dataTableModel1.getValueAt(selectedRows1[i], 0).toString();
                             deleteFromDatabase(maphongban); // Xóa dữ liệu từ cơ sở dữ liệu
                             dataTableModel1.removeRow(selectedRows1[i]);
                         }
                     }
                 } else {
                     // Thông báo rằng không có hàng nào được chọn
                     JOptionPane.showMessageDialog(null, "Vui lòng chọn ít nhất một hàng để xóa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                 }
             }

             private void deleteFromDatabase(String maphongban) {
            	    try (Connection connection = DriverManager.getConnection(url,user,password)) {
            	        String sql = "DELETE FROM `nhansu`.`phòng ban` WHERE `Mã phòng ban` = ?"; 
            	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            	            preparedStatement.setString(1, maphongban);
            	            // Thực hiện truy vấn DELETE
            	            preparedStatement.executeUpdate();
            	            System.out.println("The data has been deleted from the database");
            	        }
            	    } catch (SQLException ex) {
            	        ex.printStackTrace();
            	        // Xử lý lỗi kết nối hoặc thực hiện truy vấn
            	    }
            	}

         });

         
         
         searchss1.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 String keyword1 = searchs1.getText().trim().toLowerCase();

                 if (!keyword1.isEmpty()) {
                     // Tìm kiếm và hiển thị kết quả
                     filterTable(dataTableModel1, keyword1);
                 } else {
                     // Nếu từ khóa trống, hiển thị toàn bộ dữ liệu
                     resetTable(dataTableModel1);
                 }
             }

             private void filterTable(DefaultTableModel table1, String keyword1) {
            	    TableRowSorter<DefaultTableModel> sorter1 = new TableRowSorter<>(table1);
            	    sorter1.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword1), 0, 1, 2, 3, 4, 5, 6));
            	    dataTable1.setRowSorter(sorter1);
            	}


             private void resetTable(DefaultTableModel table1) {
                 TableRowSorter<DefaultTableModel> sorter1 = new TableRowSorter<>(table1);
                 sorter1.setRowFilter(null);
                 dataTable1.setRowSorter(sorter1);
             }
         });
         ketnoiphongMysql();

        return panel1;
    }
    public void ketnoiphongMysql() {
        try {
            
            try (Connection connection = DriverManager.getConnection(url, user, password);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM nhansu.`phòng ban`")) {

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                DefaultTableModel tableModel1 = (DefaultTableModel) dataTable1.getModel();
                tableModel1.setRowCount(0); 

                while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = resultSet.getObject(i);
                    }
                    tableModel1.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
          
        }
    }

    
    
    private JPanel Createotherprojects(String content) {
    	JPanel panel2 = new JPanel(new BorderLayout());
    	DefaultTableModel dataTableModel2 = new DefaultTableModel();
   	 dataTableModel2.addColumn("Mã dự án");
   	 dataTableModel2.addColumn("Tên dự án");
   	 dataTableModel2.addColumn("Ngân sách");
   	 dataTable2 = new JTable(dataTableModel2);
     dataTable2.getTableHeader().setBackground(new Color(70, 130, 180));
        JScrollPane scrollPane2 = new JScrollPane(dataTable2);
       

        // Adding the table and scroll bars to the panel
        panel2.add(scrollPane2, BorderLayout.CENTER);

        // Adding search components
        JPanel searchPanel2 = new JPanel(new GridLayout(1, 0));
        JLabel search2 = new JLabel("Tìm kiếm");
        JTextField searchs2 = new JTextField();
        JButton searchss2 = new JButton("Tìm kiếm");
        searchPanel2.add(search2);
        searchPanel2.add(searchs2);
        searchPanel2.add(searchss2);
        panel2.add(searchPanel2, BorderLayout.NORTH);

        // Adding buttons and input fields
        JPanel buttonAndInputPanel2 = new JPanel(new BorderLayout());
        
        JPanel inputPanel2 = new JPanel();
        inputPanel2.setLayout(new GridLayout(3, 2));
        inputPanel2.add(new JLabel("Mã dự án"));
        inputPanel2.add(new JTextField());
        inputPanel2.add(new JLabel("Tên dự án"));
        inputPanel2.add(new JTextField());
        inputPanel2.add(new JLabel("Ngân sách"));
        inputPanel2.add(new JTextField());
        
        
        inputPanel2.setBackground(new Color(0, 130, 180));
        buttonAndInputPanel2.add(inputPanel2, BorderLayout.NORTH);
        
        
        JPanel buttonPanel2 = new JPanel(new FlowLayout());
        JButton addButton2 = new JButton("Thêm");
        JButton editButton2 = new JButton("Sửa");
        JButton deleteButton2 = new JButton("Xóa");


        buttonPanel2.add(addButton2);
        buttonPanel2.add(editButton2);
        buttonPanel2.add(deleteButton2);

        buttonAndInputPanel2.add(buttonPanel2, BorderLayout.WEST);


        panel2.add(buttonAndInputPanel2, BorderLayout.SOUTH);

        // Thêm lắng nghe cho nút "Thêm"
        addButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maduan = ((JTextField) inputPanel2.getComponent(1)).getText();
                String tenduan = ((JTextField) inputPanel2.getComponent(3)).getText();
                String ngansach = ((JTextField) inputPanel2.getComponent(5)).getText();

                // Kết nối đến MySQL
                try (Connection connection = DriverManager.getConnection(url,user,password)) {
                    // Sử dụng PreparedStatement để tránh SQL Injection
                    String sql = "INSERT INTO `dự án` (`Mã dự án`, `Tên dự án`, `Ngân sách`) VALUES (?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, maduan);
                        preparedStatement.setString(2, tenduan);
                        preparedStatement.setString(3, ngansach);

                        // Thực hiện truy vấn INSERT
                        preparedStatement.executeUpdate();
                        System.out.println("The data has been added to the database");
                    }

                    // Thêm dữ liệu vào bảng
                    dataTableModel2.addRow(new Object[]{maduan, tenduan, ngansach});

                    // Xóa nội dung của các trường dữ liệu sau khi thêm
                    ((JTextField) inputPanel2.getComponent(1)).setText("");
                    ((JTextField) inputPanel2.getComponent(3)).setText("");
                    ((JTextField) inputPanel2.getComponent(5)).setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    
                }
            }
        });
        
        
        editButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow2 = dataTable2.getSelectedRow();

                if (selectedRow2 != -1) {
                    String maduan = dataTableModel2.getValueAt(selectedRow2, 0).toString();
                    String tenduan = dataTableModel2.getValueAt(selectedRow2, 1).toString();
                    String ngansach = dataTableModel2.getValueAt(selectedRow2, 2).toString();

                    // Tạo một bản sao của inputPanel để hiển thị trong dialog
                    JPanel clonedInputPanel2 = cloneInputPanel(inputPanel2);

                    // Hiển thị trên giao diện chính để sửa thông tin
                    ((JTextField) clonedInputPanel2.getComponent(1)).setText(maduan);
                    ((JTextField) clonedInputPanel2.getComponent(3)).setText(tenduan);
                    ((JTextField) clonedInputPanel2.getComponent(5)).setText(ngansach);

                    // Hiển thị dialog
                    int result2 = JOptionPane.showConfirmDialog(null, clonedInputPanel2, "Sửa Thông Tin", JOptionPane.OK_CANCEL_OPTION);

                    if (result2 == JOptionPane.OK_OPTION) {
                        // Lấy thông tin mới từ giao diện chính
                        maduan = ((JTextField) clonedInputPanel2.getComponent(1)).getText();
                        tenduan = ((JTextField) clonedInputPanel2.getComponent(3)).getText();
                        ngansach = ((JTextField) clonedInputPanel2.getComponent(5)).getText();

                        // Cập nhật dữ liệu trong bảng
                        dataTableModel2.setValueAt(maduan, selectedRow2, 0);
                        dataTableModel2.setValueAt(tenduan, selectedRow2, 1);
                        dataTableModel2.setValueAt(ngansach, selectedRow2, 2);

                        // Cập nhật dữ liệu trong cơ sở dữ liệu
                        updateDatabase(maduan, tenduan, ngansach);

                        // Thông báo rằng thông tin đã được cập nhật
                        JOptionPane.showMessageDialog(null, "Thông tin đã được cập nhật.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    // Thông báo rằng không có hàng nào được chọn
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng để sửa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            private JPanel cloneInputPanel(JPanel inputPanel2) {
                JPanel clone2 = new JPanel(new GridLayout(7, 2));

                for (Component component2 : inputPanel2.getComponents()) {
                    if (component2 instanceof JLabel) {
                        JLabel label2 = new JLabel(((JLabel) component2).getText());
                        clone2.add(label2);
                    } else if (component2 instanceof JTextField) {
                        JTextField textField2 = new JTextField();
                        clone2.add(textField2);
                    }
                }

                return clone2;
            }

            private void updateDatabase(String maduan, String tenduan, String ngansach) {
                try (Connection connection2 = DriverManager.getConnection(url,user,password)) {
                    String sql2 = "UPDATE `nhansu`.`dự án` SET `Tên dự án` = ?, `Ngân sách` = ? WHERE `Mã dự án` = ?";
                    try (PreparedStatement preparedStatement2 = connection2.prepareStatement(sql2)) {
                        preparedStatement2.setString(1, tenduan);
                        preparedStatement2.setString(2, ngansach);
                        preparedStatement2.setString(3, maduan);

                        // Thực hiện truy vấn UPDATE
                        preparedStatement2.executeUpdate();
                        System.out.println("Data updated successfully in the database."); 
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                   
                }
            }
        });
    


        
        deleteButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows2 = dataTable2.getSelectedRows();

                if (selectedRows2.length > 0) {
                    // Hỏi người dùng xác nhận việc xóa
                    int option2 = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa các hàng đã chọn?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

                    if (option2 == JOptionPane.YES_OPTION) {
                        // Xóa từng hàng được chọn
                        for (int i = selectedRows2.length - 1; i >= 0; i--) {
                            String maduan = dataTableModel2.getValueAt(selectedRows2[i], 0).toString();
                            deleteFromDatabase(maduan); // Xóa dữ liệu từ cơ sở dữ liệu
                            dataTableModel2.removeRow(selectedRows2[i]);
                        }
                    }
                } else {
                    // Thông báo rằng không có hàng nào được chọn
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn ít nhất một hàng để xóa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            private void deleteFromDatabase(String maduan) {
                try (Connection connection = DriverManager.getConnection(url,user,password)) {
                    String sql = "DELETE FROM `nhansu`.`dự án` WHERE `Mã dự án` = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, maduan);
                        // Thực hiện truy vấn DELETE
                        preparedStatement.executeUpdate();
                        System.out.println("The data has been deleted from the database");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                   
                }
            }
        });

        
        
        
        searchss2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword2 = searchs2.getText().trim().toLowerCase();

                if (!keyword2.isEmpty()) {
                    // Tìm kiếm và hiển thị kết quả
                    filterTable(dataTableModel2, keyword2);
                } else {
                    // Nếu từ khóa trống, hiển thị toàn bộ dữ liệu
                    resetTable(dataTableModel2);
                }
            }

            private void filterTable(DefaultTableModel table2, String keyword2) {
           	    TableRowSorter<DefaultTableModel> sorter2 = new TableRowSorter<>(table2);
           	    sorter2.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword2), 0, 1, 2, 3, 4, 5, 6));
           	    dataTable2.setRowSorter(sorter2);
           	}


            private void resetTable(DefaultTableModel table2) {
                TableRowSorter<DefaultTableModel> sorter2 = new TableRowSorter<>(table2);
                sorter2.setRowFilter(null);
                dataTable2.setRowSorter(sorter2);
            }
        });
        
        ketnoiduanMysql();

        return panel2;
    }
   


    private void ketnoiduanMysql() {
        try {
           
            try (Connection connection = DriverManager.getConnection(url, user, password);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM nhansu.`dự án`")) {

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                DefaultTableModel tableModel2 = (DefaultTableModel) dataTable2.getModel();
                tableModel2.setRowCount(0); // Clear existing rows

                while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = resultSet.getObject(i);
                    }
                    tableModel2.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
          
        }
    }

  
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuanLyNhanSu().setVisible(true));
    }
}
