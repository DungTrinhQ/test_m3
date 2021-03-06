package controller;

import model.Product;
import service.ProductDAOImpl;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductServlet",urlPatterns = "/home")
public class ProductServlet extends HttpServlet {
    ProductDAOImpl productDAO = new ProductDAOImpl();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action){
            case "create":
                createProduct(request,response);
                break;
            case "delete":
                deleteProduct(request,response);
                break;
            case "update":
                updateProduct(request,response);
                break;
            case "search":
                searchProduct(request,response);
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action){
            case "create":
                showCreateForm(request,response);
                break;
            case "delete":
                showDeleteForm(request,response);
                break;
            case "update":
                showUpdateForm(request,response);
                break;
            default:
                showHome(request,response);
                break;
        }
    }

    private void showHome(HttpServletRequest request,HttpServletResponse response){
        try{
            List<Product> list = productDAO.findAllProduct();
            request.setAttribute("listProduct",list);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("views/home.jsp");
            requestDispatcher.forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCreateForm(HttpServletRequest request,HttpServletResponse response){
        try {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("views/create.jsp");
            requestDispatcher.forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createProduct(HttpServletRequest request,HttpServletResponse response){
        try {
            String name = request.getParameter("name");
            int price = Integer.parseInt(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String color = request.getParameter("color");
            String description = request.getParameter("description");
            String category = request.getParameter("category");
            Product product = new Product(name,price,quantity,color,description,category);
            productDAO.insertProduct(product);
            showHome(request,response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void showDeleteForm(HttpServletRequest request,HttpServletResponse response){
        try{
            int id = Integer.parseInt(request.getParameter("id"));
            Product existingProduct = productDAO.selectProduct(id);
            request.setAttribute("product",existingProduct);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("views/delete.jsp");
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteProduct(HttpServletRequest request,HttpServletResponse response){
        try{
            int id = Integer.parseInt(request.getParameter("id"));
            productDAO.deleteProduct(id);
            showHome(request,response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void showUpdateForm(HttpServletRequest request,HttpServletResponse response){
        try{
            int id = Integer.parseInt(request.getParameter("id"));
            Product existingProduct = productDAO.selectProduct(id);
            request.setAttribute("product",existingProduct);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("views/update.jsp");
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProduct(HttpServletRequest request,HttpServletResponse response){
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            int price = Integer.parseInt(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String color = request.getParameter("color");
            String description = request.getParameter("description");
            String category = request.getParameter("category");
            Product product = new Product(id,name,price,quantity,color,description,category);
            productDAO.updateProduct(product);
            response.sendRedirect("/home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void searchProduct(HttpServletRequest request,HttpServletResponse response){
        try{
            String name = request.getParameter("search");
            List<Product> list = productDAO.selectProductByName(name);
            request.setAttribute("listProduct",list);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("views/home.jsp");
            requestDispatcher.forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}