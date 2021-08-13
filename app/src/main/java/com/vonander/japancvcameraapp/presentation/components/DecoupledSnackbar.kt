package com.vonander.japancvcameraapp.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun DecoupledSnackbar(
    snackbarHostState: SnackbarHostState,
    onclick: () -> Unit
){
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val snackbar = createRef()
        SnackbarHost(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .constrainAs(snackbar) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            hostState = snackbarHostState,
            snackbar = {
                Snackbar(
                    action = {
                        TextButton(
                            onClick = {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                onclick()
                            }
                        ){
                            Text(
                                text = snackbarHostState.currentSnackbarData?.actionLabel?: "",
                                color = MaterialTheme.colors.onPrimary,
                            )
                        }
                    },
                    backgroundColor = MaterialTheme.colors.secondary
                ) {
                    Text(
                        text = snackbarHostState.currentSnackbarData?.message?: "",
                        color = MaterialTheme.colors.onPrimary,
                    )
                }
            }
        )
    }
}