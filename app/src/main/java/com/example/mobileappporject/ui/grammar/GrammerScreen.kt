package com.example.mobileappporject.ui.grammar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GrammarScreen(
    viewModel: GrammarViewModel = viewModel()
) {

    val grammar by viewModel.grammar.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadGrammar()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "English Grammar",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {

            loading -> {
                CircularProgressIndicator()
            }

            error != null -> {
                Text(text = error ?: "Error")

                Button(
                    onClick = {
                        viewModel.loadGrammar()
                    }
                ) {
                    Text("Retry")
                }
            }

            else -> {
                LazyColumn {

                    items(grammar) { lesson ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    lesson.title,
                                    style = MaterialTheme.typography.titleLarge
                                )

                                Text(lesson.description)

                                Text(lesson.khmerExplanation)
                            }
                        }
                    }
                }
            }
        }
    }
}