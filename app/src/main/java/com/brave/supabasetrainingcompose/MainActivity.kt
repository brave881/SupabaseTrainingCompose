package com.brave.supabasetrainingcompose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brave.supabasetrainingcompose.ui.theme.SupabaseTrainingComposeTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val supabase = createSupabaseClient(
    supabaseUrl = "https://xrjstriitdochwekjtcp.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhyanN0cmlpdGRvY2h3ZWtqdGNwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDk2NjY4MDAsImV4cCI6MjAyNTI0MjgwMH0.RvOF3-cXP4J47neuRX7NOuz8jHXOETvYRpCEAjrV2To"
) {
    install(Postgrest)
    install(Auth)

//    defaultSerializer = MoshiSerializer()
}

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            SupabaseTrainingComposeTheme {
                // A surface container using the 'background' color from the theme

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "SplashScreen") {
                    composable("SplashScreen") {
                        SplashScreen(navController)
                    }
                    composable("LoginScreen") {
                        LoginScreen(navController)
                    }
                    composable("MainScreen") {
                        MainScreen(navController)
                    }
                }


            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {

    var showDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,

        ) {
        Scaffold(
            floatingActionButton = {
                Column {
                    FloatingActionButton(onClick = { showDialog = true }) {
                        if (showDialog) {
                            EmailPasswordDialog(country = null) {
                                showDialog = false
                            }
                        }
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    FloatingActionButton(onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            supabase.auth.signOut()
                            delay(2000)
                            navController.navigate("LoginScreen")
                            {
                                popUpTo("MainScreen") {
                                    inclusive = true
                                }
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    var refresh by remember {
                        mutableStateOf(false)
                    }
                    FloatingActionButton(onClick = { refresh = true }) {
                        if (refresh) {
//                           CountriesList()
                        }
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)

                    }

                }

            },
        ) {
            CountriesList()
            it
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailPasswordDialog(
    country: Country? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var countryName by remember { mutableStateOf("") }
    var countryCapital by remember { mutableStateOf("") }


    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = { Text(text = "Insert") },
        text = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = countryName,
                    onValueChange = { countryName = it },
                    label = { Text("Country name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = countryCapital,
                    onValueChange = { countryCapital = it },
                    label = { Text("Country capital name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val country1 = Country(
                        name = countryName,
                        user_id = supabase.auth.currentUserOrNull()?.id ?: "",
                        capital = countryCapital,
                    )
                    val scope = CoroutineScope(Dispatchers.IO + Job());
                    scope.launch {
                        supabase.from("countries").insert(country1)

                    }
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    // Handle cancel button click
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryEditDialog(
    country: Country,
    onDismiss: () -> Unit,
    onSave: (Country) -> Unit
) {

    val focusManager = LocalFocusManager.current
    var countryName by remember { mutableStateOf(country.name ?: "") }
    var countryCapital by remember { mutableStateOf(country.capital ?: "") }
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = { Text(text = "Edit Country") },
        text = {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = countryName,
                    onValueChange = { countryName = it },
                    label = { Text("Country name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = countryCapital,
                    onValueChange = { countryCapital = it },
                    label = { Text("Country capital name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedCountry = country.copy(
                        name = countryName,
                        capital = countryCapital
                    )
                    onSave(updatedCountry)
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    // Handle cancel button click
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CountriesList() {
    var countries by remember { mutableStateOf<List<Country>>(listOf()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            countries = supabase.from("countries")
                .select().decodeList<Country>()
        }
    }

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {

        items(
            countries,
            key = { country -> country.id ?: 0 },
        ) { country ->
            val showDialog = remember { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    country.name ?: "",
                    modifier = Modifier.padding(8.dp),
                )

                IconButton(onClick = {
                    showDialog.value = true
                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "")

                    if (showDialog.value) {
                        CountryEditDialog(
                            country = country,
                            onDismiss = { showDialog.value = false },
                            onSave = { updatedCountry ->

                                CoroutineScope(Dispatchers.Main).launch {
                                    supabase.from("countries").update(
                                        {
                                            Country::name setTo updatedCountry.name
                                            Country::capital setTo updatedCountry.capital
                                            //or
//                                    set("name", countryName)
                                        }
                                    ) {
                                        filter {
                                            Country::id eq country.id
                                            Country::user_id eq supabase.auth.currentUserOrNull()?.id
                                            //or
//                                    supabase.auth.currentUserOrNull()?.let { eq("user_id", it.id) }
                                        }
                                    }
                                }
                            }
                        )
                    }

                }

                IconButton(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        supabase.from("countries").delete {
                            filter {
                                Country::id eq country.id
                                Country::user_id eq supabase.auth.currentUserOrNull()?.id
                                //or
//                                eq("id", 666)
                            }
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "")

                }


            }
        }
    }
}

