package com.venicio.eventgo.view.ui.framgents

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.FutureTarget
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.venicio.eventgo.R
import com.venicio.eventgo.data.model.CheckIn
import com.venicio.eventgo.data.model.EventMap
import com.venicio.eventgo.databinding.CustomDialogBinding
import com.venicio.eventgo.databinding.FragmentEventDetailsBinding
import com.venicio.eventgo.viewmodel.EventsDetailViewModel
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class EventDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEventDetailsBinding
    private val dViewModel: EventsDetailViewModel by viewModels()
    private lateinit var eventMap: EventMap
    private var itemId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventDetailsBinding.inflate(layoutInflater)

        itemId = arguments?.getString("itemId")

        dViewModel.getDetailEvent(itemId.toString())

        setupObservers()

        binding.btnCheckIn.setOnClickListener { showDialog() }

        binding.btnShare.setOnClickListener {
            verifyPermission()
        }

        return (binding.root)
    }

    private fun verifyPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            createAndSharePDF()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun createAndSharePDF() {

        try {
            // Arquivo temporário para armazenar o PDF
            val pdfFile = File(requireContext().cacheDir, "EventGo.pdf")

            // Cria um novo documento PDF
            val document = Document()
            PdfWriter.getInstance(document, FileOutputStream(pdfFile))

            document.open()

            val event = dViewModel.eventDetailData

            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    // Carrega a imagem usando Glide e salva temporariamente para referenciá-la no PDF
                    val futureTarget: FutureTarget<Bitmap> = Glide.with(requireContext())
                        .asBitmap()
                        .load(event.value?.image)
                        .submit()

                    val bitmap: Bitmap = futureTarget.get()

                    // Salva a imagem temporariamente para referenciá-la no PDF
                    val imageFile = File(requireContext().cacheDir, "temp_image.png")
                    val imageOutputStream = FileOutputStream(imageFile)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOutputStream)
                    imageOutputStream.close()

                    val widthImage = 400f
                    val heightImage = 140f

                    val image = Image.getInstance(imageFile.absolutePath)
                    image.scaleAbsolute(widthImage, heightImage)
                    image.alignment = Image.ALIGN_CENTER

                    document.add(image)
                    document.add(Paragraph(event.value?.title))
                    document.add(Paragraph(converterDate(event.value?.date)))
                    document.add(Paragraph(getString(R.string.price, event.value?.price)))
                    document.add(Paragraph(event.value?.description))

                    document.close()
                }
            }

            shareFile(requireContext(), pdfFile)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Erro ao gerar o PDF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        dViewModel.eventDetailData.observe(this, Observer { eventData ->
            Glide.with(binding.ivEventDetail)
                .load(eventData.image)
                .error(R.drawable.not_load_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivEventDetail)

            binding.tvTitle.text = eventData.title
            binding.tvDateValue.text = converterDate(eventData.date)
            binding.tvPriceValue.text = getString(R.string.price, eventData.price)
            binding.tvDescriptionValue.text = eventData.description

            eventMap = EventMap(LatLng(eventData.latitude, eventData.longitude), eventData.title)

            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
            mapFragment.getMapAsync { googleMap ->
                googleMap.addMarker(
                    MarkerOptions()
                        .title(eventMap.title)
                        .position(eventMap.latLng)
                )
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        eventMap.latLng, 16f
                    )
                )
            }
        })

        dViewModel.checkInSuccess.observe(viewLifecycleOwner, Observer { check ->
            if (check) {
                showToastAndResetCheckIn()
            }
        })

        dViewModel.errorCheckIn.observe(viewLifecycleOwner, Observer {
            Toast.makeText(
                requireContext(),
                "CHECK-IN NÃO REALIZADO, TENTE NOVAMENTE MAIS TARDE",
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    private fun showToastAndResetCheckIn() {
        Toast.makeText(requireContext(), "CHECK-IN REALIZADO COM SUCESSO", Toast.LENGTH_SHORT)
            .show()

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            dViewModel.resetCheckInSuccess()
        }
    }

    private fun showDialog() {
        val build = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)

        val dialogBinding: CustomDialogBinding = CustomDialogBinding.inflate(layoutInflater)
        build.setView(dialogBinding.root)

        val dialog = build.create()
        dialog.setCancelable(false)
        dialog.show()

        dialogBinding.ibtnClose.setOnClickListener { dialog.dismiss() }

        dialogBinding.btnConfirmCheckIn.setOnClickListener {
            val name = dialogBinding.edtNome.text.toString()
            val email = dialogBinding.edtEmail.text.toString()

            if (name == "") {
                Toast.makeText(requireContext(), "Insira seu nome", Toast.LENGTH_SHORT).show()
            } else {
                if (isValidEmail(email)) {
                    sendCheckIn(CheckIn(itemId.toString(), name, email))
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Email inválido", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun sendCheckIn(checkInData: CheckIn) {
        dViewModel.sendDataCheckIn(checkInData)
    }

    private fun converterDate(date: Long?): String {
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.timeInMillis = date
        }

        val year = calendar.get(Calendar.YEAR).toString()
        val month = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH).toString()

        return getString(R.string.date, dayOfMonth, month, year)
    }

    private fun shareFile(context: Context, arquivo: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            shareFileWithFileProvider(context, arquivo)
        } else {
            shareFileBeforeAPI24(context, arquivo)
        }
    }

    private fun shareFileWithFileProvider(context: Context, file: File) {
        val uri: Uri = FileProvider.getUriForFile(context,
            "${context.packageName}.fileprovider", file
        )
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(intent, "Compartilhar PDF via"))
    }

    private fun shareFileBeforeAPI24(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
        context.startActivity(Intent.createChooser(intent, "Compartilhar PDF via"))
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 117
    }

}

