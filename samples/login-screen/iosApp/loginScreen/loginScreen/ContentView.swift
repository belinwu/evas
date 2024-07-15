//
//  ContentView.swift
//  loginScreen
//
//  Created by Sebastian Sellmair on 13.07.24.
//

import SwiftUI
import SampleAppKt

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        SampleAppViewControllerKt.createViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}


#Preview {
    ContentView()
}
    