package com.mayihi.store.web.servlet;

import com.mayihi.store.Util.BaseServlet;
import com.mayihi.store.Util.PageModel;
import com.mayihi.store.Util.UUIDUtils;
import com.mayihi.store.domain.Category;
import com.mayihi.store.domain.Product;
import com.mayihi.store.service.CategoryService;
import com.mayihi.store.service.ProductService;
import com.mayihi.store.service.serviceImpl.CategoryServiceImpl;
import com.mayihi.store.service.serviceImpl.ProductServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProductManageServlet", urlPatterns = "/ProductManageServlet")
public class ProductManageServlet extends BaseServlet {
    private ProductService productService = new ProductServiceImpl();

    public String showProductsWithPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int page = Integer.parseInt(request.getParameter("num"));
        int onShelf = Integer.parseInt(request.getParameter("onShelf"));
        PageModel pageModel = null;
        pageModel = productService.showProductsWithPage(onShelf, page);
        request.setAttribute("page", pageModel);
        if (onShelf == 0) {
            return "/admin/product/list.jsp";
        } else {
            return "/admin/product/pushDown_list.jsp";
        }
    }

    private String showProductsIn1stPage(HttpServletRequest request, HttpServletResponse response, int onShelf) throws Exception {
        PageModel pageModel = productService.showProductsWithPage(onShelf, 1);
        request.getSession().setAttribute("page", pageModel);
        if (onShelf == 1) {
            response.sendRedirect(request.getContextPath()+"/admin/product/pushDown_list.jsp");
        }
        if (onShelf == 0) {
            response.sendRedirect(request.getContextPath()+"/admin/product/list.jsp");
        }
        return null;
    }

    //    editProductUI
    public String editProductUI(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pid = request.getParameter("pid");
        Product product = new ProductServiceImpl().queryProductByPid(pid);
        CategoryService categoryService = new CategoryServiceImpl();
        List<Category> list = categoryService.getAllCats();
        request.setAttribute("product", product);
        request.setAttribute("pCats", list);
        System.out.println("原始： " + product);
        return "/admin/product/edit.jsp";
    }

    public String editProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Product product = new Product();
        Map<String, Object> map = new HashMap<>();
        System.out.println("进入addProduct");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> list = upload.parseRequest(request);
        fileBeanUtil(map, list);

        DateConverter dc = new DateConverter();
        dc.setPattern("yyyy-MM-dd");
        ConvertUtils.register(dc, java.util.Date.class);

        BeanUtils.populate(product, map);
        System.out.println("更新后： " + product);
        productService.updateProduct(product);

        return showProductsIn1stPage(request, response, 0);
    }

    /**
     * 处理multipart/form-data,具有文件输入的form表单
     *
     * @param map
     * @param list
     * @throws IOException
     */
    private void fileBeanUtil(Map<String, Object> map, List<FileItem> list) throws IOException {
        for (FileItem fileItem : list) {
            if (fileItem.isFormField()) {
                map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
            } else {
                InputStream is = fileItem.getInputStream();
                String name = fileItem.getName();
                if (null == fileItem.getName() || "".equals(fileItem.getName())) {
                    return;
                }
//                System.out.println(getServletContext().getRealPath(""));
                String path = getServletContext().getRealPath("/");
                /*上传文件的拼接格式是由jsp文件中的<img src="${ pageContext.request.contextPath }/${p.pimage}">决定，这是提前约定好的
                *其中 contextPath 指的是项目路径，因此需要将 ${p.pimage} 的路径指定为项目路径下图片的相对路径才行，在这里就是 img/upload/filename
                由于我们规定的数据库中的文件路径就是‘相对于项目的相对路径’，因此我们要按照这个规定来，才能在jsp中正确的显示出来。
                相反的，如果按照path="/img/upload/filename"  filename="1.jpg" 来创建file ： File file = new File(path, fileName);的话，虽然可以
                正确的创建出来文件，存放的位置也正确。但是filename就仅仅是1.jpg，在jsp文件中取出的时候拼接成的完整路径为contextPath/1.jpg，可见是有误的
                * */
                String fileName = "/img/upload/" + UUIDUtils.getId() + fileItem.getName();
                File file = new File(path, fileName);
                file.createNewFile();
                OutputStream os = new FileOutputStream(file);
                IOUtils.copy(is, os);
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(os);
                map.put(fileItem.getFieldName(), fileName);
            }
        }
    }

    public String addProductUI(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CategoryService categoryService = new CategoryServiceImpl();
        List<Category> list = categoryService.getAllCats();
        request.setAttribute("pCats", list);
//        request.getSession().setAttribute("pCats",list);
        return "/admin/product/add.jsp";
    }

    //    addProduct
    public String addProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Product product = new Product();
        Map<String, Object> map = new HashMap<>();
        System.out.println("进入addProduct");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> list = upload.parseRequest(request);
        fileBeanUtil(map, list);
        BeanUtils.populate(product, map);
        product.setPid(UUIDUtils.getId());
        product.setPflag(0);
        productService.addProduct(product);

        return showProductsIn1stPage(request, response, 0);
    }

    /**
     * onShelf=1,让商品下架  onShelf=0.让商品上架
     */
    public String updateShelf(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pid = request.getParameter("pid");
        int onShelf = Integer.parseInt(request.getParameter("onShelf"));
        Product product = productService.queryProductByPid(pid);
        product.setPflag(onShelf);
        productService.updateProduct(product);
        return showProductsIn1stPage(request,response,onShelf);
    }



}
