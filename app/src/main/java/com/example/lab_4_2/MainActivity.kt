package com.example.lab_4_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch

class Recipe(val name: String, val imageUrl: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val snackbarHost = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHost) }
    ) { padding ->
        MainScreen(modifier = Modifier.padding(padding), snackbarHost)
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, snackbar: SnackbarHostState) {
    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    val recipes = remember { mutableStateListOf<Recipe>() }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Healthy Living")

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("Imagen URL") },
            modifier = Modifier.fillMaxWidth()
        )
        val scope = rememberCoroutineScope()
        Button(onClick = {
            if (name.isNotBlank() && url.isNotBlank()) {
                recipes.add(Recipe(name, url))
                name = ""
                url = ""
            } else {
                // muestra mensaje de error simple
                scope.launch {
                    snackbar.showSnackbar("Faltan datos")
                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(recipes) { rec ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(rec.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = rec.name,
                            modifier = Modifier.size(100.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(rec.name)
                    }
                }
            }
        }
    }
}
