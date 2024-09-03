package eu.tutorials.myshoppinglistapp

import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.tutorials.myshoppinglistapp.ui.theme.MyShoppingListAppTheme

data class ShoppingItem(
    val id: Int, var name: String, var quantity: Int, var isEditing: Boolean = false
)


@Composable
fun ShoppingListApp() {//list of items stored in sItems-> line 58 used to make a shopping item
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {//content in the button
            //Text("Add Item")
            //Input    //output type
            // val doubleNumber:(Int)-> Int = { it*2}//lambda expression-> variable called as a function
            Text("Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems) {
                //ShoppingListItem(it, {}, {})
                    item ->// it changed to item
                if (item.isEditing) {
                    ShoppingItemEditor(
                        item = item,
                        onEditComplete = { editName, editQuantity ->
                            sItems = sItems.map { it.copy(isEditing = false) }
                            val editItem = sItems.find { it.id == item.id }
                            editItem?.let {
                                it.name = editName
                                it.quantity = editQuantity
                            }
                        }
                    )
                } else {
                    ShoppingListItem(item = item,
                        onEditClick = {
                            sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                        },
                        onDeleteClick = { sItems = sItems - item }
                    )

                }
            }
        }
    }

    if (showDialog) {
        itemQuantity = ""
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween//pushes both the button to the extreme edged of the alert box
                ) {
                    Button(onClick = {
                        if (itemName.isNotBlank()) {
                            val newItem = ShoppingItem(
                                id = sItems.size + 1,
                                name = itemName,
                                quantity = itemQuantity.toInt()
                            )
                            sItems = sItems + newItem
                            showDialog = false
                            itemName = ""
                            itemQuantity = " "
                        }
                    })
                    {
                        Text(text = "Add")
                    }
                    Button(onClick = { showDialog = false }) {
                        Text(text = "Cancel")
                    }

                }
            },
            title = { Text(text = "Add Shopping Item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {
                            itemName = it
                        },//it is the value of the outlineTextfield at any given moment
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {
                            itemQuantity = it
                        },//it is the value of the outlineTextfield at any given moment
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        )
    }
}


    @Composable
    fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String, Int) -> Unit) {
        var editName by remember { mutableStateOf(item.name) }
        var editQuantity by remember { mutableStateOf(item.quantity.toString()) }
        var isEditing by remember { mutableStateOf(item.isEditing) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly

        )
        {
            Column {
                BasicTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    singleLine = true,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp)
                )
                BasicTextField(
                    value = editQuantity,
                    onValueChange = { editQuantity = it },
                    singleLine = true,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp)
                )
            }
            Button(
                onClick = {
                    isEditing = false
                    onEditComplete(editName, editQuantity.toIntOrNull() ?: 1)
                }
            ) {
                Text(text = "Save")
            }
        }
    }


    @Composable
    fun ShoppingListItem(
// creating our own onClick function

        item: ShoppingItem,
        onEditClick: () -> Unit,//lambda Function-: No parameter ,but can be used to pass a function to a function
        onDeleteClick: () -> Unit,

        ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .border(
                    border = BorderStroke(2.dp, Color.LightGray),
                    shape = RoundedCornerShape(20)
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = item.name, modifier = Modifier.padding(8.dp))
            Text(text = "Qty ${item.quantity}", modifier = Modifier.padding(8.dp))
            Row(modifier = Modifier.padding(8.dp)) {
                IconButton(onClick = onEditClick) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)

                }
                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)

                }

            }
        }
    }


