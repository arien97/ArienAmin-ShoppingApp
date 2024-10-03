package com.example.hw4shoppingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingApp()
        }
    }
}

data class Product(val name: String, val price: String, val description: String) : Serializable

val products = listOf(
    Product("Kayali | Vanilla", "$100", "This is a 1.7oz bottle of perfume. It has key notes of Vanilla Orchids, Tonka Absolute, and Amber Woods."),
    Product("YSL | Black Opium", "$130", "This is a 1.6oz bottle of perfume. It has key notes of Black Coffee, White Flowers, and Vanilla."),
    Product("Marc Jacobs | Daisy", "$186", "This is a 6.80z bottle of perfume. It has key notes of Strawberry, Violet Leaves, and Jasmine.")
)

@Composable
fun ShoppingApp() {
    val windowInfo = calculateCurrentWindowInfo()
    var selectedProduct by rememberSaveable { mutableStateOf<Product?>(null) }

    if (windowInfo.isWideScreen) {
        Row(modifier = Modifier.fillMaxSize()) {
            ProductList(products = products, onProductSelected = { selectedProduct = it }, selectedProduct = selectedProduct, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))
            ProductDetailPane(product = selectedProduct, onGoBack = { selectedProduct = null }, isWideScreen = true, modifier = Modifier.weight(1f))
        }
    } else {
        if (selectedProduct == null) {
            ProductList(products = products, onProductSelected = { selectedProduct = it }, selectedProduct = selectedProduct, modifier = Modifier.fillMaxSize())
        } else {
            ProductDetailPane(product = selectedProduct, onGoBack = { selectedProduct = null }, isWideScreen = false, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun ProductList(products: List<Product>, onProductSelected: (Product) -> Unit, selectedProduct: Product?, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        item {
            Text(
                text = "Products",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(products) { product ->
            val isSelected = selectedProduct == product
            val backgroundColor = if (isSelected) Color(0xffdef2d8) else Color.White

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor, RoundedCornerShape(8.dp))
                    .clickable { onProductSelected(product) }
                    .padding(8.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ProductDetailPane(product: Product?, onGoBack: () -> Unit, isWideScreen: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (product != null) {
                Text(
                    text = "Details for ${product.name}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Price: ${product.price}",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.description,
                    fontSize = 16.sp
                )
                if (!isWideScreen) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "< Go back",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable { onGoBack() }
                            .padding(8.dp),
                        fontSize = 16.sp,
                        color = Color.Blue
                    )
                }
            } else {
                Text(
                    text = "No product selected",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun calculateCurrentWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val isWideScreen = screenWidth >= 600

    return WindowInfo(
        isWideScreen = isWideScreen
    )
}

data class WindowInfo(
    val isWideScreen: Boolean
)