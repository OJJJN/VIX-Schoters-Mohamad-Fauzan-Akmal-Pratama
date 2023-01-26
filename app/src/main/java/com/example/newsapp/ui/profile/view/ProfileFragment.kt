package com.example.newsapp.ui.profile.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentProfileBinding
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.RoundingParams
import com.facebook.imagepipeline.request.ImageRequestBuilder

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tvFullName.setText(R.string.full_name)
            btnGithub.setText(R.string.github_username)
            btnGithub.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse("https://github.com/mfauzanakmalpratama"))
                startActivity(intent)
            }
            ivProfile.apply {
                val imageRequest =
                    ImageRequestBuilder.newBuilderWithResourceId(R.raw.profile).build()
                val imageUriString = imageRequest.sourceUri.toString()
                setImageURI(imageUriString)

                hierarchy.apply {
                    setPlaceholderImage(R.drawable.ic_image, ScalingUtils.ScaleType.FIT_CENTER)
                    setFailureImage(R.drawable.ic_broken_image, ScalingUtils.ScaleType.FIT_CENTER)
                    roundingParams = RoundingParams.asCircle()
                }
            }
        }
    }
}