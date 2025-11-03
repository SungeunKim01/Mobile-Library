package com.example.mobile_dev_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import com.example.mobile_dev_project.R
import androidx.compose.ui.platform.testTag
import com.example.mobile_dev_project.ui.model.ImportPhase
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * screen that lets the user paste a book url and start the import pipeline Download then Unzip then Parse then Save is triggered by the vm
 *ui reads state thru vm fields and calls vm methods to mutate state*
 * vm is obtained via hiltViewModel(), since the project uses Hilt for DI
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadBookScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    vm: DownloadBookViewModel = hiltViewModel()
) {
    //spacing
    val pad = dimensionResource(id = R.dimen.space_md)

    Scaffold(
        // top app bar with a back button -back action is delegated to onBack provided by the caller
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.download_title)) },
                navigationIcon = {
                    // disable the back button while importing to avoid leaving mid process
                    IconButton(
                        onClick = onBack,
                        enabled = !vm.isImporting,
                        modifier = Modifier.testTag("BackButton")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cancel)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // page content column - fill available size so it behaves well on diff devices
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = pad, vertical = pad)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(pad)
        ) {
            // url input
            // - value = vm.url, onValueChange = vm::onUrlChanged
            // - disabled while importing to prevent edits mid process
            // - Err state shows inline via supportingText when vm.errorText is set
            OutlinedTextField(
                value = vm.url,
                onValueChange = vm::onUrlChanged,
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
                label = { Text(stringResource(R.string.download_url_label)) },
                placeholder = { Text(stringResource(R.string.download_url)) },
                visualTransformation = VisualTransformation.None,
                enabled = !vm.isImporting,
                isError = vm.errorText != null,
                supportingText = {
                    vm.errorText?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = dimensionResource(R.dimen.field_height))
                    .testTag("UrlField")
            )

            // 1st action button
            // Enabled only when the vm says the input is valid & not currently importing
            // also call vm to start the pipeline
            Button(
                onClick = vm::onAddClicked,
                enabled = vm.canAdd,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("AddButton")
            ) {
                Text(text = stringResource(R.string.download_action))
            }

            //2nd action -disabled while importing to avoid leaving half way thru the pipeline
            OutlinedButton(
                onClick = onBack,
                enabled = !vm.isImporting,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("CancelButton")
            ) {
                Text(text = stringResource(R.string.cancel))
            }

            // Progress and result
            //- vm exposes a ProgressState w phase and msg
            vm.progress?.let { step ->
                Divider()
                // message
                Text(step.message, style = MaterialTheme.typography.titleMedium)

                step.detail?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(Modifier.height(pad))

                //when pipeline reports DONE, provide clear
                if (step.phase == ImportPhase.DONE) {
                    Button(
                        onClick = onBack,
                        modifier = Modifier.testTag("DoneButton")
                    ) {
                        Text(stringResource(R.string.done))
                    }
                }
            }
        }
    }
}