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

@Composable
fun TransactionMapScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val located = remember {
        AppData.transactions.findAll().filter {

            it.latitude != 0.0 || it.longitude != 0.0
        }
    }

        val mapView = remember {
            MapView(context).apply {
                setTileSource(TileSourceFactory.OpenTopo)
                setMultiTouchControls(true)
            }
        }


        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when(event){
                    Lifecycle.Event.ON_RESUME -> mapView.onResume()
                    Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
                mapView.onDetach()
            }
        }

        Scaffold(
            topBar = {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        text = "Transaction Map",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        ) {
            paddingValues ->
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                factory = {
                    mapView.apply {
                        overlays.clear()

                        located.forEach { t ->
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

                        when{

                            located.isEmpty()->{
                                controller.setZoom(10.0)
                                controller.setCenter(GeoPoint(52.245954, -7.139539))
                            }
                            located.size == 1 -> {
                                controller.setZoom(16.0)
                                controller.setCenter(GeoPoint(located[0].latitude, located[0].longitude))
                            }

                            else -> {
                                val box = BoundingBox.fromGeoPoints(
                                    located.map { GeoPoint(it.latitude, it.longitude) }
                                )
                                post {zoomToBoundingBox(box, false, 100)}
                            }
                        }
                        invalidate()
                    }
                }
            )
        }
}