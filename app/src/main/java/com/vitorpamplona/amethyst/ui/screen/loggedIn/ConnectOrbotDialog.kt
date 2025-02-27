package com.vitorpamplona.amethyst.ui.screen.loggedIn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material3.Material3RichText
import com.vitorpamplona.amethyst.R
import com.vitorpamplona.amethyst.ui.actions.CloseButton
import com.vitorpamplona.amethyst.ui.theme.ButtonBorder
import com.vitorpamplona.amethyst.ui.theme.RichTextDefaults
import com.vitorpamplona.amethyst.ui.theme.placeholderText

@Composable
fun ConnectOrbotDialog(onClose: () -> Unit, onPost: () -> Unit, onError: (String) -> Unit, portNumber: MutableState<String>) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Surface() {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CloseButton(onPress = {
                        onClose()
                    })

                    val toastMessage = stringResource(R.string.invalid_port_number)

                    UseOrbotButton(
                        onPost = {
                            try {
                                Integer.parseInt(portNumber.value)
                            } catch (_: Exception) {
                                onError(toastMessage)
                                return@UseOrbotButton
                            }

                            onPost()
                        },
                        isActive = true
                    )
                }

                Column(
                    modifier = Modifier.padding(30.dp)
                ) {
                    val myMarkDownStyle = RichTextDefaults.copy(
                        stringStyle = RichTextDefaults.stringStyle?.copy(
                            linkStyle = SpanStyle(
                                textDecoration = TextDecoration.Underline,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    )

                    Row() {
                        Material3RichText(
                            style = myMarkDownStyle
                        ) {
                            Markdown(
                                content = stringResource(R.string.connect_through_your_orbot_setup_markdown)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = portNumber.value,
                            onValueChange = { portNumber.value = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                capitalization = KeyboardCapitalization.None,
                                keyboardType = KeyboardType.Number
                            ),
                            label = { Text(text = stringResource(R.string.orbot_socks_port)) },
                            placeholder = {
                                Text(
                                    text = "9050",
                                    color = MaterialTheme.colorScheme.placeholderText
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UseOrbotButton(onPost: () -> Unit = {}, isActive: Boolean, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier,
        onClick = {
            if (isActive) {
                onPost()
            }
        },
        shape = ButtonBorder,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primary else Color.Gray
        )
    ) {
        Text(text = stringResource(R.string.use_orbot), color = Color.White)
    }
}
