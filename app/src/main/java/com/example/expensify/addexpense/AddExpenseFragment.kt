package com.example.expensify.addexpense

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.expensify.R
import com.example.expensify.databinding.AddExpenseFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient

class AddExpenseFragment : Fragment() {

    companion object {
        const val TAG = "AddExpenseFragment"
    }

    private lateinit var binding: AddExpenseFragmentBinding

    private lateinit var viewModel: AddExpenseViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_LOCATION_PERMISSION = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_expense_fragment, container, false)

        viewModel = ViewModelProvider(this).get(AddExpenseViewModel::class.java)

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//
//        if(isPermissionGranted()) {
//            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                Log.i(TAG, "Location latitude: ${location.latitude} and longitude: ${location.longitude}")
//            }
//        } else {
//            enableLocation()
//        }



        return binding.root
    }

    private fun isPermissionGranted(): Boolean {
        return checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableLocation() {
        requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableLocation()
            }
        }
    }

}
