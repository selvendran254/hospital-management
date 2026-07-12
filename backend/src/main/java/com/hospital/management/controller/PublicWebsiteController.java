package com.hospital.management.controller;

import com.hospital.management.domain.entity.*;
import com.hospital.management.dto.request.ExtensionRequests;
import com.hospital.management.dto.response.ExtensionResponses;
import com.hospital.management.service.EmailService;
import com.hospital.management.service.ExtensionCrudService;
import com.hospital.management.service.SmsWhatsAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/public/website")
@RequiredArgsConstructor
public class PublicWebsiteController {

    private final ExtensionCrudService extensionCrudService;
    private final EmailService emailService;
    private final SmsWhatsAppService smsWhatsAppService;

    @PostMapping("/contact-support")
    public ResponseEntity<ExtensionResponses.ContactSupportResponse> contactSupport(
            @Valid @RequestBody ExtensionRequests.ContactSupportRequest request) {
        emailService.sendTemplateEmail(
                "support@hospital.com",
                "contact-support",
                "New public support request",
                Map.of("name", request.getName(), "email", request.getEmail(), "message", request.getMessage()));
        if (Boolean.TRUE.equals(request.getChatRequest())) {
            smsWhatsAppService.sendWhatsApp("+910000000000", "Chat support requested by " + request.getName());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ExtensionResponses.ContactSupportResponse.builder()
                        .ticketId(UUID.randomUUID().toString())
                        .status("RECEIVED")
                        .build());
    }

    @PostMapping("/testimonials")
    public ResponseEntity<Testimonial> createTestimonial(@Valid @RequestBody ExtensionRequests.TestimonialRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createTestimonial(request));
    }

    @GetMapping("/testimonials")
    public ResponseEntity<List<Testimonial>> getTestimonials() {
        return ResponseEntity.ok(extensionCrudService.getTestimonials());
    }

    @PutMapping("/testimonials/{id}")
    public ResponseEntity<Testimonial> updateTestimonial(@PathVariable UUID id,
                                                         @Valid @RequestBody ExtensionRequests.TestimonialRequest request) {
        return ResponseEntity.ok(extensionCrudService.updateTestimonial(id, request));
    }

    @DeleteMapping("/testimonials/{id}")
    public ResponseEntity<Void> deleteTestimonial(@PathVariable UUID id) {
        extensionCrudService.deleteTestimonial(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/faqs")
    public ResponseEntity<Faq> createFaq(@Valid @RequestBody ExtensionRequests.FaqRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createFaq(request));
    }

    @GetMapping("/faqs")
    public ResponseEntity<List<Faq>> getFaqs() {
        return ResponseEntity.ok(extensionCrudService.getFaqs());
    }

    @PutMapping("/faqs/{id}")
    public ResponseEntity<Faq> updateFaq(@PathVariable UUID id, @Valid @RequestBody ExtensionRequests.FaqRequest request) {
        return ResponseEntity.ok(extensionCrudService.updateFaq(id, request));
    }

    @DeleteMapping("/faqs/{id}")
    public ResponseEntity<Void> deleteFaq(@PathVariable UUID id) {
        extensionCrudService.deleteFaq(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/newsletter")
    public ResponseEntity<NewsletterSubscription> subscribe(@Valid @RequestBody ExtensionRequests.NewsletterSubscriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createNewsletterSubscription(request));
    }

    @GetMapping("/newsletter")
    public ResponseEntity<List<NewsletterSubscription>> listSubscriptions() {
        return ResponseEntity.ok(extensionCrudService.getNewsletterSubscriptions());
    }

    @DeleteMapping("/newsletter/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable UUID id) {
        extensionCrudService.deleteNewsletterSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/gallery")
    public ResponseEntity<HospitalGallery> createGallery(@Valid @RequestBody ExtensionRequests.HospitalGalleryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createGallery(request));
    }

    @GetMapping("/gallery")
    public ResponseEntity<List<HospitalGallery>> getGallery() {
        return ResponseEntity.ok(extensionCrudService.getGallery());
    }
}
