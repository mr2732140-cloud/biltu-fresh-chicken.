package com.biltufreshchicken.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Product(val name: String, val subtitle: String, val price: Int)
data class CartItem(val product: Product, var qty: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BiltuApp()
        }
    }
}

private val Red = Color(0xFFD72638)
private val Dark = Color(0xFF1E1E1E)
private val Cream = Color(0xFFFFF8F6)

@Composable
fun BiltuApp() {
    val products = listOf(
        Product("Fresh Chicken", "Clean & fresh • Per kg", 210),
        Product("Chicken Curry Cut", "Ready to cook • Per kg", 230),
        Product("Chicken Breast", "Fresh boneless cut", 280),
        Product("Chicken Leg Piece", "Fresh cut • Per kg", 240)
    )

    val cart = remember { mutableStateListOf<CartItem>() }
    var tab by remember { mutableStateOf(0) }
    var orderPlaced by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Red,
            secondary = Dark,
            background = Cream,
            surface = Color.White
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                "Biltu Fresh Chicken",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Fresh chicken, fast delivery",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            bottomBar = {
                NavigationBar {
                    listOf(
                        "Home" to Icons.Default.Home,
                        "Shop" to Icons.Default.Store,
                        "Cart" to Icons.Default.ShoppingCart,
                        "Account" to Icons.Default.Person
                    ).forEachIndexed { index, pair ->
                        NavigationBarItem(
                            selected = tab == index,
                            onClick = { tab = index },
                            icon = {
                                Icon(
                                    imageVector = pair.second,
                                    contentDescription = pair.first
                                )
                            },
                            label = {
                                Text(pair.first)
                            },
                            badge = {
                                if (index == 2 && cart.isNotEmpty()) {
                                    Badge {
                                        Text(
                                            cart.sumOf { it.qty }.toString()
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        ) { padding ->
            when (tab) {
                0 -> HomeScreen(
                    modifier = Modifier.padding(padding),
                    products = products,
                    cart = cart,
                    shop = { tab = 1 }
                )

                1 -> ShopScreen(
                    modifier = Modifier.padding(padding),
                    products = products,
                    cart = cart
                )

                2 -> CartScreen(
                    modifier = Modifier.padding(padding),
                    cart = cart,
                    placeOrder = { orderPlaced = true }
                )

                else -> AccountScreen(
                    modifier = Modifier.padding(padding)
                )
            }

            if (orderPlaced) {
                AlertDialog(
                    onDismissRequest = {
                        orderPlaced = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                orderPlaced = false
                                cart.clear()
                                tab = 0
                            }
                        ) {
                            Text("Done")
                        }
                    },
                    title = {
                        Text("Order placed!")
                    },
                    text = {
                        Text(
                            "Thank you. Your demo order has been created successfully."
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier,
    products: List<Product>,
    cart: MutableList<CartItem>,
    shop: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Red
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        "Fresh Chicken",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        "Fresh • Clean • Quality Checked",
                        color = Color.White
                    )

                    Spacer(
                        Modifier.height(14.dp)
                    )

                    Button(
                        onClick = shop,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Red
                        )
                    ) {
                        Text(
                            "Order Now",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            Text(
                "Today's Fresh Items",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(products.size) { index ->
            ProductCard(
                product = products[index],
                cart = cart
            )
        }
    }
}

@Composable
fun ShopScreen(
    modifier: Modifier,
    products: List<Product>,
    cart: MutableList<CartItem>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Shop",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(products.size) { index ->
            ProductCard(
                product = products[index],
                cart = cart
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    cart: MutableList<CartItem>
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFFE6E8)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "🍗",
                    fontSize = 30.sp
                )
            }

            Spacer(
                Modifier.width(12.dp)
            )

            Column(
                Modifier.weight(1f)
            ) {
                Text(
                    product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                Text(
                    product.subtitle,
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Text(
                    "₹${product.price}",
                    color = Red,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Button(
                onClick = {
                    val existing = cart.find {
                        it.product.name == product.name
                    }

                    if (existing == null) {
                        cart.add(
                            CartItem(product, 1)
                        )
                    } else {
                        existing.qty++
                    }
                }
            ) {
                Text("Add")
            }
        }
    }
}

@Composable
fun CartScreen(
    modifier: Modifier,
    cart: MutableList<CartItem>,
    placeOrder: () -> Unit
) {
    if (cart.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.Gray
                )

                Text(
                    "Your cart is empty",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Add fresh chicken from the shop.",
                    color = Color.Gray
                )
            }
        }
    } else {
        val subtotal = cart.sumOf {
            it.product.price * it.qty
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Your Cart",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                Modifier.height(12.dp)
            )

            cart.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            Modifier.weight(1f)
                        ) {
                            Text(
                                item.product.name,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "₹${item.product.price} × ${item.qty}"
                            )
                        }

                        IconButton(
                            onClick = {
                                if (item.qty > 1) {
                                    item.qty--
                                } else {
                                    cart.remove(item)
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Remove,
                                contentDescription = "Decrease"
                            )
                        }

                        Text(
                            item.qty.toString(),
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            onClick = {
                                item.qty++
                            }
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Increase"
                            )
                        }
                    }
                }
            }

            Spacer(
                Modifier.weight(1f)
            )

            Text(
                "Subtotal: ₹$subtotal",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                Modifier.height(10.dp)
            )

            Button(
                onClick = placeOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    "Place Demo Order",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AccountScreen(
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            "Account",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            Modifier.height(16.dp)
        )

        Text("Biltu Fresh Chicken")

        Text(
            "Live phone, WhatsApp, address and order history will be connected in the next version.",
            color = Color.Gray
        )
    }
}
