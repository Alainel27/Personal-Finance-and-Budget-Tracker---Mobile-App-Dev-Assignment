package com.example.mad_ca1_27.activities


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.mad_ca1_27.main.AppData
import com.example.mad_ca1_27.models.Transaction
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

//Ai was used in help with creating this class. it was mainly used to help in understanding and implementing of the code
@Composable
fun TransactionMapScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    //this retrieves the transactions coords
    val located = remember {
        //retrieves all transactions
        AppData.transactions.findAll().filter {

            //creates list containing transactions with the condition
            it.latitude != 0.0 && it.longitude != 0.0
        }
    }

    //this creates the map of OSM
        val mapView = remember {
            MapView(context).apply {
                //setting the map style to Open Topo as the one in the lab does not work for some reason i couldn't fix
                //repeated attempts to fix gave the same 403 error
                setTileSource(TileSourceFactory.OpenTopo)
                setMultiTouchControls(true)
            }
        }



        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                //checks which lifecycle event occured
                when(event){

                    Lifecycle.Event.ON_RESUME -> mapView.onResume()
                    Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            //cleanup
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
                mapView.onDetach()
            }
        }

    //screen layout
        Scaffold(
            topBar = {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        //Heading on
                        text = "Transaction Map",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        ) {
            paddingValues ->
            //displays the android map
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                //creates the visible map
                factory = {
                    mapView.apply {
                        //removes any additional information on the map
                        overlays.clear()

                        //loops thru the transactions
                        //located is the list of transactions with coords
                        located.forEach { t ->
                            //adds them as pins and shows all the information
                            overlays.add(
                                Marker(this).apply {
                                    position = GeoPoint(t.latitude, t.longitude)
                                    title = t.title
                                    snippet = (if(t.isIncome) "+ " else "- ") +
                                            "€%.2f".format(t.amount) + " . " + t.category
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                }
                            )
                        }

                        //if there are transaction with coords than it will set the zoom at 10 and put the center on setu waterford
                        when{

                            located.isEmpty()->{
                                controller.setZoom(10.0)
                                controller.setCenter(GeoPoint(52.245954, -7.139539))
                            }
                            //if there are exactly one then it will zoom on the one transaction in the list
                            located.size == 1 -> {
                                controller.setZoom(16.0)
                                controller.setCenter(GeoPoint(located[0].latitude, located[0].longitude))
                            }

                            //if there are multiple coords than it will calculate the a box around the pins
                            else -> {
                                val box = BoundingBox.fromGeoPoints(
                                    located.map { GeoPoint(it.latitude, it.longitude) }
                                )
                                post {zoomToBoundingBox(box, false, 100)}
                            }
                        }
                        //refreshes the map
                        invalidate()
                    }
                }
            )
        }
}