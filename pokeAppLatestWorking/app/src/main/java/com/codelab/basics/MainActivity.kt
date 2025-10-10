/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


// Started with Android Developer code sample
// Code available at https://developer.android.com/codelabs/jetpack-compose-theming#0
// ...then swapped in a DB to populate the compose list

package com.codelab.basics

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codelab.basics.ui.theme.BasicsCodelabTheme
import com.codelab.basics.ui.theme.Blue

/**
 * Sample DB Compose app with Master-Details pages
 * ShowPageMaster ... shows the list of DB elements
 * ShowPageDetails ... shows the detail contents of one element
 *
 * Added Adaptive behavior...
 *  - show master and details on different screens
 *  - if landscape, show master and details side-by-side
 *
 * Use the logcat to follow the logic.
 *
 * It's waiting for real data....
 */
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Open the DB
        val DBtest = DBClass(this@MainActivity)
        Log.d("CodeLab_DB", "onCreate: ")
        val initialNames = DBtest.findAll()

        // Then the real data
        setContent {
            BasicsCodelabTheme {
                MyApp(
                    modifier = Modifier.fillMaxSize()
                    // Get the data from the DB for display
                    , initialNames,
                    DBtest
                )
            }
        }
    }
}

@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    initialNames: List<DataModel>,
    DBtest: DBClass
) {
    val windowInfo = rememberWindowInfo()  // get size of this screen
    var index by remember { mutableIntStateOf(-1) } // which name to display
    var showMaster by remember {mutableStateOf(true)} // fudge to force master list first, when compact
    var names by remember {mutableStateOf(DBtest.findAll())}

    val refreshData: () -> Unit = {
        names = DBtest.findAll()
        Log.d("CodeLab_DB", "Data Refreshed. New size: ${names.size}")
    }

    Surface(modifier, color = Color(141, 153, 174)) { // change this for app background colour
        // either one page at a time, or both side-by-side
        Log.d(
            "CodeLab_DB",
            "MyApp0: index = $index "
        )
        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
            if (showMaster or ((index < 0) or (index >= names.size))) {      //Always Check endpoints!
                Log.d("CodeLab_DB", "MyApp1: index = $index firstTime = $showMaster")
                showMaster = false
                ShowPageMaster(names = names,
                    updateIndex = { index = it }
                , DBtest = DBtest,
                    refreshData = refreshData)
            } else {
                Log.d("CodeLab_DB", "MyApp2: $index ")
                ShowPageDetails(name = names[index],  // List starts at 0, DB records start at 1
                    index = index,               // use index for prev, next screen
                    updateIndex = { index = it },
                    refreshData = refreshData)
            }
        } else {  // show master details side-by-side
            // force visible entry if index=-1
            if (index < 0) {
                index = 0
            }
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Blue)
                ) {
                    ShowPageMaster(names = names,
                        updateIndex = { index = it },
                        DBtest = DBtest,
                        refreshData = refreshData)
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Red) // idk what this colour does
                ) {
                    ShowPageDetails(name = names[index],  // List starts at 0, DB records start at 1
                        index = index,               // use index for prev, next screen
                        updateIndex = { index = it },
                        refreshData = refreshData)
                }
            }
        }
    }
}

@Composable
private fun ShowPageMaster(
    modifier: Modifier = Modifier,
    names: List<DataModel>,
    updateIndex: (index: Int) -> Unit,
    DBtest: DBClass,
    refreshData: () -> Unit
) {
    val mostAccessedPokemon = names.maxByOrNull{it.accessCount}
    Column(modifier = modifier){
        if(mostAccessedPokemon != null){
            MostAccessedItemCard(
                name = mostAccessedPokemon,
                pos = names.indexOf(mostAccessedPokemon),
                updateIndex = updateIndex,
                DBtest = DBtest,
                refreshData = refreshData
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            itemsIndexed(items = names) { pos, name ->
                Log.d("CodeLab_DB", "Item at index $pos is $name")
//            if(name.id != mostAccessedPokemon?.id){
//                ShowEachListItem(name = name, pos, updateIndex, DBtest, refreshData)
//            }
                ShowEachListItem(name = name, pos, updateIndex, DBtest, refreshData)
            }
        }
    }



}

@Composable
private fun MostAccessedItemCard(
    name: DataModel,
    pos: Int,
    updateIndex: (index: Int) -> Unit,
    DBtest: DBClass,
    refreshData: () -> Unit
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFDD835) // this makes the most accessed pokemon box yellow
        ),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ){
            Text(
                text = "Most Accessed Pokemon",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black // text in most accessed box
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "${name.pokeName} (${name.accessCount} views)",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color.Black // text that tells you how many views the most accessed has
                )
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    onClick = {
                        updateIndex(pos);
                        DBtest.incAccessCount(name.id)
                        refreshData()
                    }
                ) {Text(text = "Details")}
            }
        }
    }
}

@Composable
private fun ShowEachListItem(
    name: DataModel,
    pos: Int,
    updateIndex: (index: Int) -> Unit,
    DBtest: DBClass,
    refreshData: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name, pos, updateIndex, DBtest, refreshData)
        Log.d("CodeLab_DB", "Greeting: ")
    }
}

@Composable
private fun CardContent(
    name: DataModel,
    pos: Int,
    updateIndex: (index: Int) -> Unit,
    DBtest: DBClass,
    refreshData: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red, //details button colour
                    contentColor = Color.White //text in details button
                ),
                onClick = {
                    updateIndex(pos);
                    Log.d(
                        "CodeLab_DB",
                        "Clicked = ${name.toString()} "
                    )
                    // call to increment access count
                    DBtest.incAccessCount(name.id)
                    refreshData()
                    Log.d("CodeLab_DB", "Inc Access = ${name.toString()} ")
                })
            { Text(text = "Details ${pos + 1}") } // pos plus one so it shows 1-6 instead of 0-5
            Text(
                // Just the name of this record
                text = name.pokeName,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )

            if (expanded) {
                Text(
                    text = (name.toString())  // Full toString of data


                    //text = "${name.accessCount}"
                )
                Log.d("CodeLab_DB", "Expanded name = ${name.toString()} ")
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Filled.ExpandLess else Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }
    }
}


@Composable
private fun ShowPageDetails(
    name: DataModel,
    updateIndex: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    index: Int,
    refreshData: () -> Unit
) {
    val windowInfo = rememberWindowInfo()  // sorta global, not good
    Column(
        modifier = modifier.fillMaxWidth(0.5f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(name.toString())
        Log.d("CodeLab_DB", "ShowData: $name.toString()")

        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
            Button(onClick = { updateIndex(-1) })
            { Text(text = "Master") }
        }
        // need check for end of array
        Button(onClick = { updateIndex(index + 1) })
        { Text(text = "Next") }
        if (index > 0) {
            Button(onClick = { updateIndex(index - 1) })
            { Text(text = "Prev") }
        }
    }
}