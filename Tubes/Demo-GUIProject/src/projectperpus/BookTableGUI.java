/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectperpus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author rakha
 */

public class BookTableGUI extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;

    public BookTableGUI() {
        setTitle("Book Table");
        setSize(800, 400); // Lebar frame ditambah agar ada ruang untuk sidebar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tambahkan sidebar
        JPanel sidebarPanel = new JPanel(new GridLayout(2, 0));
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setBackground(Color.LIGHT_GRAY);
        add(sidebarPanel, BorderLayout.WEST);
        
        // Model tabel
        model = DatabaseConnection.fetchDataTabelBuku();

        // Tabel
        table = new JTable(model);

        // Scroll pane untuk tabel
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Membuat komponen pencarian
        JPanel searchPanel = new JPanel(new GridBagLayout());
        JLabel searchLabel = new JLabel("Cari Judul:"); // Tambahkan label "Cari Judul"
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset"); // Tombol reset

        // Menggunakan GridBagConstraints untuk menetapkan constraint
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.0; // Label tidak perlu menggunakan semua lebar yang tersedia
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(searchLabel, gbc);

        gbc.weightx = 1.0; // Menggunakan seluruh lebar yang tersedia
        gbc.gridx = 1;
        searchPanel.add(searchField, gbc);

        gbc.weightx = 0.0; // Button tidak perlu menggunakan semua lebar yang tersedia
        gbc.gridx = 2;
        searchPanel.add(searchButton, gbc);

        gbc.gridx = 3; // Penempatan tombol reset
        searchPanel.add(resetButton, gbc);

        // Tambahkan action listener untuk tombol reset
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hapus filter dari tabel
                refreshTable();
                // Reset isi dari searchField
                searchField.setText("");
            }
        });

        // Tambahkan panel pencarian ke frame
        getContentPane().add(searchPanel, BorderLayout.NORTH);

        // Menambahkan action listener ke tombol pencarian
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().toLowerCase();
                if (!searchText.isEmpty()) {
                    searchByTitle(searchText);
                } else {
                    // Jika kolom pencarian kosong, tampilkan semua baris
                    refreshTable();
                }
            }
        });

        // Tombol untuk tambah
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tampilkan dialog tambah data
                showAddDialog();
            }
        });

        // Tombol untuk edit
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mendapatkan indeks baris yang dipilih pada tampilan tabel
                int selectedViewRow = table.getSelectedRow();
                if (selectedViewRow == -1) {
                    JOptionPane.showMessageDialog(BookTableGUI.this, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Mendapatkan indeks baris asli dalam model data sebelum menerapkan sorter
                int selectedModelRow = table.convertRowIndexToModel(selectedViewRow);

                // Memeriksa apakah baris yang dipilih saat ini difilter atau tidak
                if (table.getRowSorter() != null) {
                    int modelIndex = table.getRowSorter().convertRowIndexToModel(selectedViewRow);
                    if (modelIndex != selectedModelRow) {
                        JOptionPane.showMessageDialog(BookTableGUI.this, "Cannot edit filtered row.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Ambil data dari baris yang dipilih
                String id = (String) model.getValueAt(selectedModelRow, 0);
                String title = (String) model.getValueAt(selectedModelRow, 1);
                String author = (String) model.getValueAt(selectedModelRow, 2);
                int pages = (int) model.getValueAt(selectedModelRow, 3);
                Date publicationDate = (Date) model.getValueAt(selectedModelRow, 4); // Get Date object

                // Tampilkan dialog edit data
                showEditDialog(id, title, author, pages, publicationDate);
            }
        });

        // Tombol untuk hapus
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedViewRow = table.getSelectedRow();
                if (selectedViewRow != -1) {
                    int confirm = JOptionPane.showConfirmDialog(BookTableGUI.this, "Are you sure you want to delete this book?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        int selectedModelRow = table.convertRowIndexToModel(selectedViewRow);
                        if (table.getRowSorter() != null) {
                            selectedModelRow = table.getRowSorter().convertRowIndexToModel(selectedViewRow);
                        }
                        String id = (String) model.getValueAt(selectedModelRow, 0);
                        DatabaseConnection.deleteBook(id);
                        model.removeRow(selectedModelRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(BookTableGUI.this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Panel untuk tombol tambah/edit
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Tambahkan tombol-tombol di sidebar
        JButton sidebarButton1 = new JButton("Sidebar Button 1");
        JButton sidebarButton2 = new JButton("Sidebar Button 2");
        sidebarPanel.add(sidebarButton1);
        sidebarPanel.add(sidebarButton2);

        setVisible(true);
    }

    // Method untuk pencarian berdasarkan judul dan penyaringan tabel
    private void searchByTitle(String title) {
        ArrayList<Integer> foundRows = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            String currentTitle = model.getValueAt(i, 1).toString().toLowerCase();
            if (currentTitle.contains(title)) {
                foundRows.add(i);
            }
        }
        filterTable(foundRows);
    }

    // Method untuk menyaring tabel berdasarkan baris yang ditemukan
    private void filterTable(ArrayList<Integer> foundRows) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                return foundRows.contains(entry.getIdentifier());
            }
        });
    }

    // Method untuk menyegarkan tabel (menampilkan semua baris)
    private void refreshTable() {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
        sorter.setRowFilter(null);
    }

    // Metode untuk menampilkan dialog tambah data
    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "Add Book", true);
        dialog.setSize(500, 200);
        dialog.setLayout(new GridLayout(7, 2));

        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField pagesField = new JTextField();
        JTextField dateField = new JTextField(); // Date field

        dialog.add(new JLabel("ID (String):"));
        dialog.add(idField);
        dialog.add(new JLabel("Title (String):"));
        dialog.add(titleField);
        dialog.add(new JLabel("Author (String):"));
        dialog.add(authorField);
        dialog.add(new JLabel("Pages (Int):"));
        dialog.add(pagesField);
        dialog.add(new JLabel("Publication Date (YYYY-MM-DD):")); // Label for date
        dialog.add(dateField);

        // Komponen kosong untuk menambahkan spasi sebelum tombol "Add"
        dialog.add(new JLabel(""));
        dialog.add(new JLabel(""));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the dialog
                dialog.dispose();
            }
        });

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = idField.getText();
                    String title = titleField.getText();
                    String author = authorField.getText();
                    int pages = Integer.parseInt(pagesField.getText());
                    Date publicationDate = getDateFromString(dateField.getText()); // Get Date from String

                    // Cek apakah input tanggal tidak null
                    if (publicationDate == null) {
                        throw new ParseException("Publication date is not valid.", 0);
                    }

                    DatabaseConnection.insertDataBuku(id, title, author, pages, publicationDate);
                    // Add data to the table
                    model.addRow(new Object[]{id, title, author, pages, publicationDate});
                    // Jika berhasil, tampilkan pesan sukses
                    JOptionPane.showMessageDialog(BookTableGUI.this, "Data berhasil ditambahkan!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(BookTableGUI.this, ex, "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Close the dialog
                dialog.dispose();
            }
        });

        dialog.add(cancelButton);
        dialog.add(addButton);

        dialog.setVisible(true);
    }

    // Metode untuk menampilkan dialog edit data
    private void showEditDialog(String id, String title, String author, int pages, Date publicationDate) {
        JDialog dialog = new JDialog(this, "Edit Book", true);
        dialog.setSize(500, 200);
        dialog.setLayout(new GridLayout(7, 2));

        JTextField idField = new JTextField(id);
        idField.setEditable(false); // ID cannot be edited
        JTextField titleField = new JTextField(title);
        JTextField authorField = new JTextField(author);
        JTextField pagesField = new JTextField(String.valueOf(pages));
        JTextField dateField = new JTextField(getStringFromDate(publicationDate)); // Date field with date string

        dialog.add(new JLabel("ID (String):"));
        dialog.add(idField);
        dialog.add(new JLabel("Title (String):"));
        dialog.add(titleField);
        dialog.add(new JLabel("Author (String):"));
        dialog.add(authorField);
        dialog.add(new JLabel("Pages (Int):"));
        dialog.add(pagesField);
        dialog.add(new JLabel("Publication Date (YYYY-MM-DD):")); // Label for date
        dialog.add(dateField);

        // Komponen kosong untuk menambahkan spasi sebelum tombol "Add"
        dialog.add(new JLabel(""));
        dialog.add(new JLabel(""));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the dialog
                dialog.dispose();
            }
        });

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(BookTableGUI.this, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    DatabaseConnection.updateBookData(id, title, author, pages, publicationDate);
                    // Update data in the table
                    model.setValueAt(titleField.getText(), selectedRow, 1);
                    model.setValueAt(authorField.getText(), selectedRow, 2);
                    model.setValueAt(Integer.parseInt(pagesField.getText()), selectedRow, 3);
                    Date newPublicationDate = getDateFromString(dateField.getText(), publicationDate); // Get updated date

                    // Cek apakah input tanggal tidak null
                    if (publicationDate == null) {
                        throw new ParseException("Publication date is not valid.", 0);
                    }

                    model.setValueAt(newPublicationDate, selectedRow, 4);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(BookTableGUI.this, "Please enter valid data.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Close the dialog
                dialog.dispose();
            }
        });
        dialog.add(cancelButton);
        dialog.add(editButton);

        dialog.setVisible(true);
    }

    // Metode untuk mengonversi string menjadi objek Date
    private Date getDateFromString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(BookTableGUI.this, "Please enter valid date", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Metode untuk mengonversi string menjadi objek Date
    private Date getDateFromString(String dateString, Date initialDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(BookTableGUI.this, "Please enter valid date", "Error", JOptionPane.ERROR_MESSAGE);
            return initialDate;
        }
    }

    // Metode untuk mengonversi objek Date menjadi string
    private String getStringFromDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookTableGUI();
            }
        });
    }
}

