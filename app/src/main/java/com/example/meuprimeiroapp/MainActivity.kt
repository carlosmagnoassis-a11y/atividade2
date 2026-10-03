package com.example.meuprimeiroapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meuprimeiroapp.ui.theme.MeuPrimeiroAppTheme

data class Movimentacao(
    val descricao: String,
    val valor: Double,
    val isReceita: Boolean
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeuPrimeiroAppTheme {
                TelaControleFinanceiro()
            }
        }
    }
}

@Composable
fun TelaControleFinanceiro() {
    var descricao by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var Plano by remember { mutableStateOf(true) }
    var isReceita by remember { mutableStateOf(true) }
    var mensagemFeedback by remember { mutableStateOf("") }
    val movimentacoes = remember { mutableStateListOf<Movimentacao>() }


    val saldoTotal = movimentacoes.sumOf {
        if (it.isReceita) it.valor else -it.valor
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Ícone do Aplicativo",
            modifier = Modifier.size(56.dp),
            tint = Color.Blue
        )


        Text(
            text = "Controle Financeiro",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Registe e organize as suas movimentações diárias.",
            fontSize = 14.sp,

        )


        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Resumo Mensal", fontWeight = FontWeight.Bold)
                Text(
                    text = "Saldo Atual: R$ %.2f".format(saldoTotal),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (saldoTotal >= 0) Color.Green else Color.Red,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição (ex: Almoço, Cantina)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = valor,
            onValueChange = { valor = it },
            label = { Text("Valor (R$)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = dia,
            onValueChange = { descricao = it },
            label = { Text("Descrição (ex: data de hoje)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = if (isReceita) "Tipo: Receita (+)" else "Tipo: Despesa (-)")
            Switch(
                checked = isReceita,
                onCheckedChange = { isReceita = it }
            )

            Text(text = if (Plano) "Tipo: planejado (+)" else "Tipo: nao planejado (-)")
            Switch(
                checked = Plano,
                onCheckedChange = { Plano = it }
            )
        }
        if (mensagemFeedback.isNotEmpty()) {
            Text(
                text = mensagemFeedback,
                color = Color.Blue,
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = {
                val valorDouble = valor.replace(",", ".").toDoubleOrNull()
                if (descricao.isNotBlank() && valorDouble != null && valorDouble > 0) {
                    movimentacoes.add(Movimentacao(descricao, valorDouble, isReceita))
                    mensagemFeedback = "Movimentação guardada com sucesso!"
                    descricao = ""
                    valor = ""
                } else {
                    mensagemFeedback = "Por favor, preencha a descrição e um valor válido."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Movimentação")
        }


        if (movimentacoes.isNotEmpty()) {
            Text(
                text = "Histórico de Movimentações",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 8.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movimentacoes) { mov ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.LightGray
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = mov.descricao, fontWeight = FontWeight.Medium)
                            Text(
                                text = "${if (mov.isReceita) "+" else "-"} R$ %.2f".format(mov.valor),
                                color = if (mov.isReceita) Color.Green else Color.Red,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
