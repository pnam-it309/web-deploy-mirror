<script setup lang="ts">
import { ref } from 'vue';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { useToast } from '@/components/ui/toast/use-toast';

const { toast } = useToast();

const file = ref<File | null>(null);
const title = ref('');
const description = ref('');
const isUploading = ref(false);

const handleFileChange = (e: Event) => {
  const target = e.target as HTMLInputElement;
  if (target.files && target.files[0]) {
    file.value = target.files[0];
  }
};

const handleSubmit = async () => {
  if (!file.value) {
    toast({
      title: 'Error',
      description: 'Please select a file to upload',
      variant: 'destructive',
    });
    return;
  }

  isUploading.value = true;

  try {
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1500));

    toast({
      title: 'Success',
      description: 'File uploaded successfully',
    });

    // Reset form
    file.value = null;
    title.value = '';
    description.value = '';
    const fileInput = document.getElementById('file-upload') as HTMLInputElement;
    if (fileInput) fileInput.value = '';

  } catch (error) {
    console.error('Upload failed:', error);
    toast({
      title: 'Error',
      description: 'Failed to upload file. Please try again.',
      variant: 'destructive',
    });
  } finally {
    isUploading.value = false;
  }
};
</script>

<template>
  <div class="container mx-auto py-8">
    <Card class="max-w-2xl mx-auto">
      <CardHeader>
        <CardTitle>Upload New Product</CardTitle>
        <CardDescription>
          Add a new product to the catalog by filling out the form below.
        </CardDescription>
      </CardHeader>

      <CardContent class="space-y-6">
        <div class="space-y-2">
          <Label for="title">Product Title</Label>
          <Input id="title" v-model="title" placeholder="Enter product title" />
        </div>

        <div class="space-y-2">
          <Label for="description">Description</Label>
          <Textarea
            id="description"
            v-model="description"
            placeholder="Enter product description"
            class="min-h-[100px]"
          />
        </div>

        <div class="space-y-2">
          <Label for="file-upload">Product Image</Label>
          <div class="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-dashed rounded-md">
            <div class="space-y-1 text-center">
              <svg
                class="mx-auto h-12 w-12 text-gray-400"
                stroke="currentColor"
                fill="none"
                viewBox="0 0 48 48"
                aria-hidden="true"
              >
                <path
                  d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                />
              </svg>
              <div class="flex text-sm text-gray-600">
                <label
                  for="file-upload"
                  class="relative cursor-pointer bg-white rounded-md font-medium text-primary-600 hover:text-primary-500 focus-within:outline-none focus-within:ring-2 focus-within:ring-offset-2 focus-within:ring-primary-500"
                >
                  <span>Upload a file</span>
                  <input
                    id="file-upload"
                    name="file-upload"
                    type="file"
                    class="sr-only"
                    @change="handleFileChange"
                  />
                </label>
                <p class="pl-1">or drag and drop</p>
              </div>
              <p class="text-xs text-gray-500">
                PNG, JPG, GIF up to 10MB
              </p>
              <p v-if="file" class="text-sm text-gray-500 mt-2">
                Selected: {{ file.name }}
              </p>
            </div>
          </div>
        </div>
      </CardContent>

      <CardFooter class="flex justify-end">
        <Button @click="handleSubmit" :disabled="isUploading">
          <span v-if="isUploading" class="mr-2">
            <svg class="animate-spin h-4 w-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
          </span>
          {{ isUploading ? 'Uploading...' : 'Upload Product' }}
        </Button>
      </CardFooter>
    </Card>
  </div>
</template>
