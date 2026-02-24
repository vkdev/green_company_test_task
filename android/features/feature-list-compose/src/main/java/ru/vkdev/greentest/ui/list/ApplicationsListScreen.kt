package ru.vkdev.greentest.ui.list

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ApplicationsListScreen(paddingValues: PaddingValues) {
    ApplicationsListScreenContent(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    )
}

@Composable
internal fun ApplicationsListScreenContent(
    modifier: Modifier,
    viewModel: ApplicationsListViewModel = koinViewModel(key = "ApplicationsListScreen")
) {

    LaunchedEffect(viewModel) {
        viewModel.startLoading()
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(horizontal = 10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state.runnableOnly, onCheckedChange = { checked ->
                viewModel.handleIntent(ApplicationsListViewModel.ShowRunnableOnlyIntent(checked))
            })
            Spacer(Modifier.width(5.dp))
            Text(stringResource(R.string.runnable_only))
        }

        Spacer(Modifier.height(5.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(
                items = state.applications,
                key = { it.packageId }
            ) { app ->
                ListItem(item = app, requestDrawable = { packageId ->
                    viewModel.requestAppIcon(packageId)
                })
            }
        }
    }
}

@Composable
internal fun ListItem(item: ApplicationsListViewModel.UiAppInfo, requestDrawable: suspend (String) -> Drawable?) {
    Card(
        Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            Modifier
                .heightIn(40.dp)
                .padding(vertical = 5.dp, horizontal = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val drawable by produceState<Drawable?>(initialValue = null, item.packageId) {
                value = requestDrawable(item.packageId)
            }

            AsyncImage(
                modifier = Modifier.size(36.dp),
                model = drawable,
                contentDescription = null
            )

            Spacer(Modifier.width(10.dp))

            Column() {
                Text(item.appName)
                Text(item.packageId)
            }
        }
    }
}