package com.Kohquqe3.Vurnaf.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Kohquqe3.Vurnaf.R
import com.Kohquqe3.Vurnaf.core.theme.AccentGold
import com.Kohquqe3.Vurnaf.core.theme.SurfaceCard
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary
import com.Kohquqe3.Vurnaf.core.theme.TextSecondary

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        containerColor = SurfaceCard,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = stringResource(R.string.pause_title),
                style = MaterialTheme.typography.titleMedium,
                color = AccentGold
            )
        },
        text = {
            Text(
                text = stringResource(R.string.pause_body),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(onClick = onRestart) {
                    Text(
                        text = stringResource(R.string.cta_restart),
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
                TextButton(onClick = onResume) {
                    Text(
                        text = stringResource(R.string.cta_resume),
                        style = MaterialTheme.typography.labelMedium,
                        color = AccentGold
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onMenu) {
                Text(
                    text = stringResource(R.string.cta_menu),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
            }
        }
    )
}
