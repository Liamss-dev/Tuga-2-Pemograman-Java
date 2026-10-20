package com.budiluhur.product_catalog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.budiluhur.product_catalog.exception.ProductNotFoundException;
import com.budiluhur.product_catalog.model.Product;
import com.budiluhur.product_catalog.service.ProductService;

@SpringBootApplication
public class ProductCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductCatalogApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(ProductService productService) {
		return args -> {
			productService.createProduct(new Product("PRD-03", "Monitor Gaming 24 Inch", 2100000.0));
			System.out.println("Daftar Produk di Spring Container:");
			productService.getAllProducts().forEach(System.out::println);

			System.out.println("--- INFORMASI HARGA & DISKON ---");
			printProductDiscountInfo(productService, "PRD-01", 10.0);
			printProductDiscountInfo(productService, "PRD-02", 0.0);
		};
	}

	private void printProductDiscountInfo(ProductService productService, String id, double discount) {
		try {
			Product product = productService.getProductById(id);
			double finalPrice = productService.getDiscountedProductPrice(id, discount);

			DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("id-ID"));
			symbols.setGroupingSeparator('.');
			symbols.setDecimalSeparator(',');
			DecimalFormat rupiahFormat = new DecimalFormat("Rp#,##0.00", symbols);

			String discountInfo = (discount > 0)
					? discount + "% (Ada Diskon)"
					: "0% (Tidak Ada Diskon)";

			System.out.println("[" + product.getId() + "] " + product.getName());
			System.out.println("- Harga Normal : " + rupiahFormat.format(product.getPrice()));
			System.out.println("- Diskon : " + discountInfo);
			System.out.println("- Harga Akhir : " + rupiahFormat.format(finalPrice));
		} catch (ProductNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}

}
